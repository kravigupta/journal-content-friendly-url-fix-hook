journal-content-friendly-url-fix-hook
=====================================

When we use Asset publisher to list all the web contents but expect web content to be opened in a different page in a specific portlet, Liferay uses the friendly url of that web content to navigate to other page and show that in Asset publisher. 

This particular scenario is mostly used for news. You list all the news but when a news is viewed, you open it in full view on another page.

A problem exists if we use special characters in title of web content, it creates some problem. Problem is that, Liferay replaces most of the special characters while generating unique url for the web content but fails to recognize all. That leads to existence of special characters in title and Liferay fails to show that web content in specific portlet.

Solution was simple, replace all special characters which Liferay code missed. Liferay takes care of these characters in FriendlyURLNormalizerImpl. Changing FriendlyURLNormalizerImpl was not easier as per our project requirements so I fixed JournalContentLocalServiceImpl instead. I created a service wrapper hook and changed the code for addArticle(...)

This code replaces the special characters from existing title and updates article. This hook must be present before we create a web content.

Steps to deploy:
================
This hook deploys just like other hot deployable plugins of Liferay.<br/>
1. If you have the source code, build it to a war file.<br/>
2. Put the package file in deploy folder of Liferay Portal.<br/>
3. Make sure you create web contents after deployment is done, this hook can not fix existing web contents.<br/>

Link to blog posts:
===================

1. [Liferay] Show Web Content in a different page using Asset Publisher
http://techdc.blogspot.in/2014/11/liferay-show-web-content-in-different.html

2. [Liferay] [Hook] Fix for Special Characters in Friendly URL of Web Content
http://techdc.blogspot.in/2014/11/liferay-hook-fix-for-special-characters.html
