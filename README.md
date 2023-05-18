# MDM_Project2
LN2 für Modul "Model Deployment &amp; Maintenance" @ ZHAW.

# Local installation
- Run the Python Script located in ONNX_Exporter to export the Python model to ONNX format.
- Copy the exported model to **src\main\resources\static\model**.
- Start the springboot application by starting the class located at **src\main\java\ch\zhaw\pfistdo1\mdm\project2\Project2Application.java**.

# Install docker image
- Start the docker image with command `docker run -p 8000:8080 project2`
