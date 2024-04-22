# Parliament-Browser

We developed a Parliament Browser with a Java backend and a JavaScript frontend. Its features include downloading, reading, analysing and storing parliamentary debates, NLP processing, database querying, RESTful web service implementation, visualisation and access management.

With the Parliament Browser, you can view and analyse various parliamentary data from the German Bundestag from the legislative periods 19 and 20. The parliamentary minutes published on the website of the German Bundestag serve as the data basis.

### Getting started:

1. **Select "Current File"** (top right, next to the hammer). A Dropdown Menu should open.

2. **In the dropdown menu select:** "Edit Configurations..."

3. **Select "+"** (top left, under the IntelliJ Sign).

4. **Select "Application"**.

5. **In the red outlined box "Main class,"** select the clipboard looking icon. If you hover over it, you can see: "Browse...(Umschalt+Eingabe)".

6. **A pop-up window ("Choose Main Class")** will open. Choose "StartAPI".

7. **Select "OK"** in this window.

8. **Select "Modify options".**

9. **Select "Shorten command line"** in the dropdown menu.

10. **Select "JAR manifest"** from the "Shorten command line" option.

11. **Select "OK"** in the "Run/Debug Configurations" window.

12. **Start the Application.**

13. **Open** http://localhost:4567/ **in the browser of your choice.**

### Assumptions:

**Regarding networks in general:**
- Upon the first visit to the page, the edges are limited to 500 to avoid unnecessary waiting times. 
- The user can later increase the limit via the filter options.

**Regarding category networks:**
- We have assumed that all speeches have the same ddc categories, only with different scores. Therefore, the ddc nodes are limited to 98.

**User Credentials:**
- **Admin Role:** User1, password: pwU1.
- **Manager Role:** User2, password: pwU2.
- **User Role:** User3, password: pwU3.

**Regarding Visualizations:**
- We have assumed that this only concerns speeches (not comments).

## Group Members:
- Jiayu
- Hamid
- Kevin
- Matthias
- Rodiana
