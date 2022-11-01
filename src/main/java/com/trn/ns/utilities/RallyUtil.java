package com.trn.ns.utilities;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.SimpleDateFormat;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

public class RallyUtil {
	
	private static RallyRestApi restApi;
	private static URI url;
	private static String usrname = "";
	private static String password = "";
	private static String workspace = "";
	private static String apiKey = "";
	private static String workspaceRef = "";


	public RallyUtil(URI url, String userName, String password, String workspace) {
		RallyUtil.restApi = null;
		RallyUtil.url = url;
		RallyUtil.usrname = userName;
		RallyUtil.password = password;
		RallyUtil.workspace = workspace;
	}

	public RallyUtil(URI url, String apiKey, String workspace) {
		RallyUtil.restApi = null;
		RallyUtil.url = url;
		RallyUtil.apiKey = apiKey;
		RallyUtil.workspace = workspace;
	}

	@SuppressWarnings("deprecation")
	private void startSession() throws Exception {
		if (!RallyUtil.apiKey.isEmpty()) {
			restApi = new RallyRestApi(RallyUtil.url, RallyUtil.apiKey);
		} else {
			restApi = new RallyRestApi(RallyUtil.url, RallyUtil.usrname, RallyUtil.password);
		}
		if (!RallyUtil.workspace.isEmpty()) {
			getWorkspaceRef();
		}
	}

	private void endSession() {
		try {
			restApi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getWorkspaceRef() throws Exception{
		String workspaceName = "";

		QueryResponse subscriptionQueryResponse = getResponse("SUBSCRIPTIONS", "", "");
		QueryRequest workspaceRequest = new QueryRequest(subscriptionQueryResponse.getResults().get(0).getAsJsonObject().getAsJsonObject("Workspaces"));
		JsonArray myWorkspaces = restApi.query(workspaceRequest).getResults();

		for (int i=0; i<myWorkspaces.size(); i++){
			workspaceName = myWorkspaces.get(i).getAsJsonObject().get("Name").getAsString();
			if(workspaceName.equals(RallyUtil.workspace)){
				RallyUtil.workspaceRef = myWorkspaces.get(i).getAsJsonObject().get("_ref").getAsString();
				break;
			}
		}
	}

	private String getUserReference(String user) throws Exception {
		QueryResponse userQueryResponse = getResponse("User", "UserName", user);
		JsonArray userQueryResults = userQueryResponse.getResults();
		JsonElement userQueryElement = userQueryResults.get(0);
		JsonObject userQueryObject = userQueryElement.getAsJsonObject();
		return userQueryObject.get("_ref").getAsString();
	}

	private QueryResponse getResponse(String type, String key, String value) throws Exception {
		QueryRequest request;

		switch (type.toUpperCase()) {
		case "TESTSET":
			type = "TestSet";
			break;
		case "TESTCASE":
			type = "TestCase";
			break;
		case "DEFECT":
			type = "Defect";
			break;
		case "REQUIREMENT":
			type = "HierarchicalRequirement";
			break;
		case "USER":
			type = "User";
			break;
		case "SUBSCRIPTIONS":
			type = "Subscriptions";
			break;		

		default:
			//it means that the type is not queryrequest but a json collection
			type = "";
		}

		if (type.isEmpty()) {
			throw new NullPointerException("Type contains no value");
		}

		request = new QueryRequest(type);
		if (!type.equals("Subscriptions") && !RallyUtil.workspace.isEmpty()) {
			request.setWorkspace(RallyUtil.workspaceRef);
		}
		if (!key.isEmpty()) {
			request.setQueryFilter(new QueryFilter(key, "=", value));
		}
		return restApi.query(request);
	}

	

	public String updateTestCase(String testCaseID, String testerName, String verdict, String build, String notes,
			String attachmentPath, String attachmentName, String attachmentDesc) {
		QueryResponse testCaseQueryResponse;

		String message = null, userRef = null;

		try {
			startSession();
			testCaseQueryResponse = getResponse("TestCase", "FormattedID", testCaseID);
			message = testCaseID + "\t" + verdict.toUpperCase();
			if (testCaseQueryResponse.wasSuccessful()) {
				int testcasecount = testCaseQueryResponse.getTotalResultCount();
				if (testcasecount == 0) {
					message = message + "\t" + "Failed to update" + "\t" + "No test case found with id";
				} else if (testcasecount == 1) {
					String fetchedTestCaseID = testCaseQueryResponse.getResults().get(0).getAsJsonObject()
							.get("FormattedID").getAsString();
					if (testCaseID.equalsIgnoreCase(fetchedTestCaseID)) {
						String testCaseRef = testCaseQueryResponse.getResults().get(0).getAsJsonObject().get("_ref")
								.getAsString();

						// Read User if required to updated the tester filed
						if (!testerName.isEmpty()) {
							userRef = getUserReference(testerName);
						}

						// Add a Test Case Result
						JsonObject newTestCaseResult = new JsonObject();
						newTestCaseResult.addProperty("Verdict", verdict);
						newTestCaseResult.addProperty("Build", build);
						newTestCaseResult.addProperty("Notes", notes);
						newTestCaseResult.addProperty("TestCase", testCaseRef);
						if (!RallyUtil.workspace.isEmpty())
							newTestCaseResult.addProperty("Workspace", RallyUtil.workspaceRef);
						if (!testerName.isEmpty())
							newTestCaseResult.addProperty("Tester", userRef);

						java.util.Date date = new java.util.Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
						String timestamp = sdf.format(date);
						newTestCaseResult.addProperty("Date", timestamp);

						CreateRequest createRequest = new CreateRequest("testcaseresult", newTestCaseResult);
						CreateResponse createResponse = restApi.create(createRequest);

						if (createResponse.wasSuccessful()) {
							String testCaseResultRef = Ref
									.getRelativeRef(createResponse.getObject().get("_ref").getAsString());

							String attachmentFullName = null;
							if (!attachmentPath.isEmpty()) {
								attachmentFullName = attachmentPath + "\\" + attachmentName;
								try {
									RandomAccessFile imageFileHandle;
									String imageBase64String;
									long attachmentSize;

									// Open file
									imageFileHandle = new RandomAccessFile(attachmentFullName, "r");

									// Get and check length
									long longLength = imageFileHandle.length();
									long maxLength = 5000000;
									if (longLength >= maxLength)
										throw new IOException("File size >= 5 MB Upper limit for Rally.");
									int fileLength = (int) longLength;

									// Read file and return data
									byte[] fileBytes = new byte[fileLength];
									imageFileHandle.readFully(fileBytes);
									imageFileHandle.close();
									imageBase64String = Base64.encodeBase64String(fileBytes);
									attachmentSize = fileLength;

									// First create AttachmentContent from image
									// string
									JsonObject attachmentContent = new JsonObject();
									attachmentContent.addProperty("Content", imageBase64String);
									if (!RallyUtil.workspace.isEmpty())
										attachmentContent.addProperty("Workspace", RallyUtil.workspaceRef);

									CreateRequest attachmentContentCreateRequest = new CreateRequest(
											"AttachmentContent", attachmentContent);
									CreateResponse attachmentContentResponse = restApi
											.create(attachmentContentCreateRequest);
									String attachmentContentRef = attachmentContentResponse.getObject().get("_ref")
											.getAsString();
									// System.out.println("Attachment Content
									// created: " + attachmentContent);

									// Now create the Attachment itself
									JsonObject attachment = new JsonObject();
									attachment.addProperty("TestCaseResult", testCaseResultRef);
									attachment.addProperty("Content", attachmentContentRef);
									attachment.addProperty("Name", attachmentName);
									attachment.addProperty("Description", attachmentDesc);
									attachment.addProperty("ContentType", "image/jpg");
									attachment.addProperty("Size", attachmentSize);
									if (!RallyUtil.workspace.isEmpty())
										attachment.addProperty("Workspace", RallyUtil.workspaceRef);
									if (!testerName.isEmpty())
										attachment.addProperty("User", userRef);

									CreateRequest attachmentCreateRequest = new CreateRequest("Attachment", attachment);
									CreateResponse attachmentResponse = restApi.create(attachmentCreateRequest);
									//String attachmentRef = attachmentResponse.getObject().get("_ref").getAsString();
									// System.out.println("Attachment created: "
									// + attachmentRef);

									if (attachmentResponse.wasSuccessful()) {
										message = message + "\t" + "Successfully updated with attachment";
									} else {
										message = message + "\t" + "Successfully updated";
										String[] attachmentContentErrors;
										attachmentContentErrors = attachmentResponse.getErrors();
										message = message + "\t" + "Error occurred during attaching file "
												+ attachmentContentErrors.toString();
									}
								} catch (Exception e) {
									message = message + "\t" + "Error occurred during attaching file "
											+ e.getStackTrace();
								}
							} else {
								message = message + "\t" + "Successfully updated and having no attachment";
							}
						} else {
							message = message + "\t" + "Failed to update" + "\t"
									+ "Error occurred while creating result";
						}
					} else {
						message = message + "\t" + "Failed to update" + "\t" + "While fetching " + testCaseID
								+ " test case id fetched " + fetchedTestCaseID;
					}
				} else {
					message = message + "\t" + "Failed to update" + "\t" + "More than one test case found";
				}
			} else {
				message = message + "\t" + "Failed to update" + "\t" + "Error occurred while fetching test case id";
			}
			// System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			endSession();
		}
		return message;
	}

	public String getStoryReference(String storyID) throws IOException{


		QueryRequest storyRequest = new QueryRequest("HierarchicalRequirement");
		storyRequest.setFetch(new Fetch("FormattedID","Name","Changesets"));
		storyRequest.setQueryFilter(new QueryFilter("FormattedID", "=", storyID));
		QueryResponse storyQueryResponse;
		storyQueryResponse = restApi.query(storyRequest);
		JsonObject storyJsonObject = storyQueryResponse.getResults().get(0).getAsJsonObject();
		String storyRef = storyJsonObject.get("_ref").toString().replace("\"", "");

		return storyRef;
	}

	public String createDefect(String projectRef,String userName, String storyID, String defectTitle, String defectDescription, String foundInBuild,String environment,
			String attachment, String attachmentName , String attachementDesc){


		String message = "";

		try{

			startSession();

			String userRef = getUserReference(userName);
			String storyRef = getStoryReference(storyID);			

			JsonObject newDefect = new JsonObject();
			newDefect.addProperty("Name", defectTitle);
			newDefect.addProperty("Project", projectRef);
			newDefect.addProperty("Description", defectDescription);
			newDefect.addProperty("Requirement", storyRef);
			newDefect.addProperty("SubmittedBy", userRef.toString());
			newDefect.addProperty("FoundInBuild", foundInBuild);
			newDefect.addProperty("Environment", environment);

			CreateRequest createRequest = new CreateRequest("defect", newDefect);
			CreateResponse createResponse = restApi.create(createRequest);

			if (createResponse.wasSuccessful()) {


				//Read defect
				String ref = Ref.getRelativeRef(createResponse.getObject().get("_ref").getAsString());

				message= "Defect "+createResponse.getObject().get("FormattedID")+" is successfully created ";

				if(!attachment.isEmpty()){
					String imageBase64String;
					long attachmentSize;

					// Open file
					RandomAccessFile myImageFileHandle = new RandomAccessFile(attachment, "r");

					try {
						long longLength = myImageFileHandle.length();
						long maxLength = 5000000;
						if (longLength >= maxLength) 
							throw new IOException("File size >= 5 MB Upper limit for Rally.");
						int fileLength = (int) longLength;            

						// Read file and return data
						byte[] fileBytes = new byte[fileLength];
						myImageFileHandle.readFully(fileBytes);
						imageBase64String = Base64.encodeBase64String(fileBytes);
						attachmentSize = fileLength;

						// First create AttachmentContent from image string
						JsonObject myAttachmentContent  = new JsonObject();
						myAttachmentContent.addProperty("Content", imageBase64String);
						CreateRequest attachmentContentCreateRequest = new CreateRequest("AttachmentContent", myAttachmentContent);
						CreateResponse attachmentContentResponse = restApi.create(attachmentContentCreateRequest);
						String myAttachmentContentRef = attachmentContentResponse.getObject().get("_ref").getAsString();
						     	

						// Now create the Attachment itself
						JsonObject myAttachment = new JsonObject();
						myAttachment.addProperty("Artifact", ref);
						myAttachment.addProperty("Content", myAttachmentContentRef);
						myAttachment.addProperty("Name", attachmentName);
						myAttachment.addProperty("Description", attachementDesc);
						myAttachment.addProperty("ContentType","image/png");
						myAttachment.addProperty("Size", attachmentSize);
						myAttachment.addProperty("User", userRef);   

						CreateRequest attachmentCreateRequest = new CreateRequest("Attachment", myAttachment);
						CreateResponse attachmentResponse = restApi.create(attachmentCreateRequest);
						String myAttachmentRef = attachmentResponse.getObject().get("_ref").getAsString();
						if (attachmentResponse.wasSuccessful()) {
							message = message + "\t" + "with Attachment";
						} else {
							String[] attachmentContentErrors;
							attachmentContentErrors = attachmentResponse.getErrors();
							message = message + "\t" + "Error occurred creating Attachment: ";
							for (int j=0; j<attachmentContentErrors.length;j++) {
								message = message +"\t"+(attachmentContentErrors[j]);
							}
						}
					}catch (Exception e) {
						System.out.println("Exception occurred while attempting to create Content and/or Attachment: ");
						e.printStackTrace();        	
					}
				}else{

					message = message + " having no attachment";
				}

			} else {
				String[] createErrors;
				createErrors = createResponse.getErrors();
				message = "Error occurred creating a defect: ";
						
				for(int j =0 ;j<createErrors.length;j++)
				message = message + "\t" +createErrors[j];

			}


		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			endSession();
		}

		return message;

	}

}