# Medical_Data_Analyser
This is written in Java and Apache Storm. This code parses a text file and ranks the most frequently occurred word in the text file.

Make sure that you have Java, Maven and Apache Storm installed on your machine. Also, place the PATIENTDATA.TXT in C drive. (This you can change depending on your location)

Steps for running this program:
1) Open your command prompt, make sure you are in the folder where POM.XML is present.
2) Run "mvn clean install" (This will create a TARGET Folder. If it is already present, then it will delete and create a fresh folder)
3) Once, TARGET folder is created, type "cd Taget" in cmd.
4) Type "storm jar MedicalDataAnalyser-1.0-SNAPSHOT.jar src.main.java.storm.MedicalDataAnalyserTopology" (Make sure you have PATIENTDATA.TXT in C Drive)

Histogram will be generated based on the data.

