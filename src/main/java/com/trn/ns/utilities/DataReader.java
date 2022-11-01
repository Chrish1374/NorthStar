package com.trn.ns.utilities;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.trn.ns.page.constants.PatientXMLConstants;

public class DataReader {

	/**
	 * @author payala2
	 * @param Patient tag name and file path
	 * @return: Require Value
	 * Description: This method return the value for given tag name(PatientValue)
	 */
	public static String getPatientDetails(String patientValue,String filePath) {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		String reqData = doc.getElementsByTagName(patientValue).item(0).getTextContent();
		fXmlFile = null;
		return reqData;

	}
	/**
	 * @author payala2
	 * @param Study tag name value and file path
	 * @return: Require Value
	 * Description: This method return the value for given tag name(StudyValue)
	 */
	public static String getStudyDetails(String studyValue,String StudyName ,String filePath) {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){
					String reqData = doc.getElementsByTagName(studyValue).item(temp).getTextContent();
					return reqData;

				}
			}
		}
		fXmlFile = null;
		return null;
	}
	/**
	 * @author payala2
	 * @param Study and Series tag name and file path
	 * @return: Require Value
	 * Description: This method return the value for given tag name(SeriesValue)
	 */
	public static String getSeriesDesc(String seriesValue,String StudyName, String SeriesName ,String filePath) {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){

					NodeList nList1 = doc.getElementsByTagName(PatientXMLConstants.SERIES_TAG);
					for(int temp1 =0 ; temp1 < nList1.getLength(); temp1++) {
						Node nNode1 = nList1.item(temp1);

						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) nNode1;

							if(eElement1.getAttribute(PatientXMLConstants.SERIESID_TAG).equalsIgnoreCase(SeriesName)){

								String reqData = doc.getElementsByTagName(seriesValue).item(temp1).getTextContent();
								return reqData;
							}
						}
					}
				}
			}
		}
		fXmlFile = null;
		return null;
	}
	/**
	 * @author vaishalip
	 * @param Study name and file path
	 * @return: Number of series 
	 * Description: This method return the value of total number of series
	 */
	public static int getNumberOfSeries(String StudyName, String filePath){
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalSeries =0;
		
		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){
					
					NodeList nList1 = doc.getElementsByTagName(PatientXMLConstants.SERIES_TAG);
					for(int temp1 =0 ; temp1 < nList1.getLength(); temp1++) {
						Node nNode1 = nList1.item(temp1);
						
						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) nNode1;
													
							if(eElement1.getAttribute(PatientXMLConstants.SERIESID_TAG).contains(StudyName)){						
								totalSeries++;
							}
							
						}
					}
				}
			}
		}
		return totalSeries;
	}

	/**
	 * @author Vivek
	 * @param Study and Result tag name and file path
	 * @return: Require Value
	 * Description: This method return the value for given tag name result tag
	 */
	public static String getResultDesc(String ResultValue,String StudyName, String ResultName ,String filePath) {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){

					NodeList nList1 = doc.getElementsByTagName(PatientXMLConstants.RESULT_TAG);
					for(int temp1 =0 ; temp1 < nList1.getLength(); temp1++) {
						Node nNode1 = nList1.item(temp1);

						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) nNode1;

							if(eElement1.getAttribute(PatientXMLConstants.SERIESID_TAG).equalsIgnoreCase(ResultName)){

								String reqData = doc.getElementsByTagName(ResultValue).item(temp1).getTextContent();
								return reqData;
							}
						}
					}
				}
			}
		}
		fXmlFile = null;
		return null;
	}

	public static String getPRDesc(String prValue,String StudyName, String prName ,String filePath) {
		
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){

					NodeList nList1 = doc.getElementsByTagName(PatientXMLConstants.PR_TAG);
					for(int temp1 =0 ; temp1 < nList1.getLength(); temp1++) {
						Node nNode1 = nList1.item(temp1);

						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) nNode1;

							if(eElement1.getAttribute(PatientXMLConstants.SERIESID_TAG).equalsIgnoreCase(prName)){

								String reqData = doc.getElementsByTagName(prValue).item(temp1).getTextContent();
								return reqData;
							}
						}
					}
				}
			}
		}
		fXmlFile = null;
		return null;
	}
		
	public static int getNumberOfResult(String StudyName, String filePath){
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalResult =0;
		
		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){
					
					NodeList nList1 = doc.getElementsByTagName(PatientXMLConstants.RESULT_TAG);
					for(int temp1 =0 ; temp1 < nList1.getLength(); temp1++) {
						Node nNode1 = nList1.item(temp1);
						
						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) nNode1;
													
							if(eElement1.getAttribute(PatientXMLConstants.SERIESID_TAG).contains(StudyName)){						
								totalResult++;
							}
							
						}
					}
				}
			}
		}
		return totalResult;
	}
	
	public static int getNumberOfPREntries(String StudyName, String filePath){
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalResult =0;
		
		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.STUDY_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				if(eElement.getAttribute(PatientXMLConstants.STUDYNAME_TAG).equalsIgnoreCase(StudyName)){
					
					NodeList nList1 = doc.getElementsByTagName(PatientXMLConstants.PR_TAG);
					for(int temp1 =0 ; temp1 < nList1.getLength(); temp1++) {
						Node nNode1 = nList1.item(temp1);
						
						if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) nNode1;
													
							if(eElement1.getAttribute(PatientXMLConstants.SERIESID_TAG).contains(StudyName)){						
								totalResult++;
							}
							
						}
					}
				}
			}
		}
		return totalResult;
	}
	
	// get the total number of contours 	
	public static int getNumberOfContours(String filePath){
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.CONTOURS_TAG);
		return nList.getLength();
	}
	
	public static String getContourProperty(String contourName,String propertyName,String filePath) {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName(PatientXMLConstants.CONTOURS_TAG);
		for(int temp =0 ; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				if(eElement.getAttribute(PatientXMLConstants.SERIESID_TAG).equalsIgnoreCase(contourName)){

					String reqData = doc.getElementsByTagName(propertyName).item(temp).getTextContent();
					return reqData;
							}
						}
				
			
		}
		fXmlFile = null;
		return null;
	}
}
