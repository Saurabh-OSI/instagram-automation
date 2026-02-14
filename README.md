# Instagram Automation

## Tech Stack
- Java
- Selenium WebDriver
- pom.xml for dependency

## Description
This project automates Instagram login and cancels pending follow requests using Selenium WebDriver with Java.
It handles dynamic elements using explicit waits and XPath strategies.

## Features
- Automated login and pop-up handling
- Data-driven execution using JSON input
- Explicit waits for stable execution

## Steps to download JSON data
1. Open your Instagram in a browser
2. Open settings and click Personal details
3. Now click "your information and permissions."
4. Now, click export your info and choose your profile if shown
5. Now, click export to device
6. In the notify field, type the email address where you want to send your data.
7. Click Customize information and uncheck all fields except followers and following in the connection block.
8. Now save and choose the data range according to your needs.
9. Only choose JSON as a file format; do not set it to HTML.
10. Now enter your Instagram password for identity confirmation.
11. Now, click continue and wait until your data is sent to your provided email.
12. Now download the zip file by clicking the "export your information" line, which will show in your email.
13. Now extract your zip file and find the "pending_follow_requests.json" file and paste this file into your project data folder.

## How to Run
1. Configure ChromeDriver
2. Update Instagram credentials
3. Run `InstagramAutomation.java`

## Must know this
1. To download JSON data of your pending requests ( Which user sent in the past), please download only followers and following data in JSON format by providing a mail address in the notify field.

## Do this in your pom.xml terminal
1. type- mvn clean install
2. Now run your Java file from the source code terminal
3. Please update the username and password filed in the code by your original username and password for automated login.

