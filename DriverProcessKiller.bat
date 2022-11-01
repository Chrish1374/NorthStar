@ECHO OFF
TASKLIST | find /i "chromedriver.exe" && taskkill /im chromedriver.exe /F || echo Process "Chrome Driver" not running.
TASKLIST | find /i "geckodriver.exe" && taskkill /im geckodriver.exe /F || echo Process "Gecko Driver" not running.
TASKLIST | find /i "IEDriverServer.exe" && taskkill /im IEDriverServer.exe /F || echo Process "IE Driver" not running.
TASKLIST | find /i "MicrosoftWebDriver.exe" && taskkill /im MicrosoftWebDriver.exe /F || echo Process "Edge Driver" not running.