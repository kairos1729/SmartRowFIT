Todo:
Add laps and rows
Laps data can come from csv - may have to calculate the average power per lap using work measurements, 
the rows marking the laps can be found using the tcx

Use this site to view .fit data:
https://www.fitfileviewer.com/

The example SmartRow files are SR1.tcx and SR1.csv
There is an example concept2 fit file, and the csv file that the garmin FitGen.exe program produces

csv to fit:
java -jar ../java/FitCSVTool.jar -c blah_re1.csv blah_re1.fit 

fit to csv:
java -jar ../java/FitCSVTool.jar -b blah_re.fit blah_re1.csv -re -u

