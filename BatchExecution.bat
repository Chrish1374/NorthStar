@title = NorthStar Automation Test Runner
@ECHO Killng all Driver Process

call DriverProcessKiller.bat

CLS
@ECHO OFF
SET "browser=chrome"
SET "server=localhost"
set /p browser=Enter Browser Name:  (e.g firefox, internet explorer, chrome, edge) 
set /p server= Enter Server IP on which build is Deployed:  (e.g localhost, 172.16.20.128) 

CLS
ECHO Select Option for Automated Test
ECHO 1.Entire Regression inclusive E2E Workflow
ECHO 2.E2E Workflows 
ECHO 3.Specific Package 
ECHO 4.Specific Feature File 
ECHO.


CHOICE /C 1234 /M "Enter your choice:"

:: Note - list ERRORLEVELS in decreasing order
IF ERRORLEVEL 4 GOTO SpecificClassFile
IF ERRORLEVEL 3 GOTO SpecificPackage
IF ERRORLEVEL 2 GOTO E2E
IF ERRORLEVEL 1 GOTO Regression


:Regression
ECHO Entire Regression
ECHO Please import DICOM data from \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation\E2E_Workflow before running test
PAUSE
call mvn clean test -DsuiteXmlFile=packageTestng.xml  -DremoteHost=%server% -Dbrowser=%browser%
GOTO End

:E2E
ECHO E2E Test
ECHO Please import DICOM data from \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation\E2E_Workflow  before running test
PAUSE
call mvn clean test -DsuiteXmlFile=testngE2E.xml  -DremoteHost=%server% -Dbrowser=%browser%
GOTO End

:SpecificPackage
ECHO Below are specific Package name
ECHO.
ECHO basic
ECHO patientList
ECHO viewer
ECHO viewer.GSPS
ECHO E2EWorkflow
ECHO.
ECHO Please import DICOM data from \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation before running test
PAUSE
ECHO.
set /p package= Enter package name to test: 
call mvn clean test -DsuiteXmlFile=packageTestng.xml -DremoteHost=%server% -Dbrowser=%browser% -Dtest=%package%.**
GOTO End

:SpecificClassFile
ECHO Below are specific features File
ECHO.
ECHO  For Basic Test:                            DICOM Data location
ECHO ------------------------                    ------------------------
ECHO DateAndTimeVerificationTest                 \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation\ViewerPackage
ECHO HeaderVerificationTest
ECHO LoginTest
ECHO NavigationBetweenAppPages
ECHO VerticalScrollbarVerificationTest

ECHO.
ECHO For PatientList Test:                       DICOM Data location
ECHO ------------------------                    ------------------------
ECHO PatientListTest                           \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation\ViewerPackage
ECHO SinglePatientStudyTest
ECHO StudyListTest

ECHO.
ECHO For Viewer Test:                            DICOM Data location
ECHO ------------------------                    ------------------------     
ECHO ActiveOverlayTest                           \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation\ViewerPackage
ECHO ContentSelectorTest                               
ECHO ContextMenuTest                                
ECHO DisplayOverlayTest                             
ECHO DistanceMeasurementTest     
ECHO DoubleClickOneUpTest
ECHO ImageOrientationTest
ECHO KeyboardEventsTest
ECHO MultiMonitorConfigurationTest
ECHO OrientationTest                  
ECHO PanSynchronizationTest                         
ECHO PdfDisplayTest 
ECHO RadialMenuTest    
ECHO ScrollSyncFromActiveOverlay
ECHO ScrollSyncTest                         
ECHO SupportedModalitiesTest                        
ECHO ViewerLayoutTest                               
ECHO WindowLevelSyncTest           
ECHO ZoomSyncFromActiveOverlay                 
ECHO ZoomSynchronizationTest                        
                          
ECHO.
ECHO For Viewer GSPS Test:                            DICOM Data location
ECHO ------------------------                    ------------------------     
ECHO CircleAnnotationTest                           \\172.16.50.149\sqa\DATACENTER\Northstar\NorthStar-Automation\ViewerPackage\GSPS
ECHO EllipseImageAnnotationTest                               
ECHO PointAnnotationTest                                
ECHO.

ECHO Please import respective DICOM data before running test
PAUSE
ECHO.
set /p feature= Enter feature file to test: 
call mvn clean test -DsuiteXmlFile=packageTestng.xml -DremoteHost=%server% -Dbrowser=%browser% -Dtest=%feature%*
GOTO End

:End
pause