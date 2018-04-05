# Bubble Sheet Scanner
 computer vision system that uses java and openCV library to read and grade bubble sheet tests.  <br />
# Steps this system do to grade student sheet

here is the sheet image
<br> <br>
![screen](1.png)
<br> <br>
1- erode the sheet image <br>
![Screenshot](2.png)
<br> <br>
2- dilate the eroded image <br>
<br>
![Screenshot](3.png) 
<br>
<br>
3- detect the centers of the two bottom circles using Hough Transform. using these centers we can <br>
calculate the corners coordinates of the slanted sheet image. <br><br>
4- wrap the dilated  sheet image using the matrix of corner coordinates of slanted image<br> <br>
![Screenshot](5.png)
 <br> <br>
5- erode the wraped sheet image <br> <br>
![Screenshot](6.png)
 <br> <br>
6- perform thresholding to make student shaded circles very clear <br> <br>
![Screenshot](7.png) 
<br> <br>
7- by calculating the intenisity of answer circles, one can determine which circle is
shaded..and if there are more than one shadded circle..then the answer is considered wrong. <br> <br>
8- student grade can be calculated by comparing the model answers which is hard coded by the
student answers.<br> <br>
9- student grade is printed out to the console.<br>

