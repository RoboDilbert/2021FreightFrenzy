package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@TeleOp(name = "Concept: TensorFlow Object Detection Webcam", group = "Concept")

public class TensorFlowTest extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = { "Ball", "Cube", "Duck", "Marker" };
    private static final String VUFORIA_KEY = "AW/D0F3/////AAABmT6CO76ZukEWtNAvh1kty819QDEF9SG9ZxbfUcbjoxBCe0UcoTGK19TZdmHtWDwxhrL4idOt1tdJE+h9YGDtZ7U/njHEqSZ7jflzurT4j/WXTCjeTCSf0oMqcgduLTDNz+XEXMbPSlnHbO9ZnEZBun7HHr6N06kpYu6QZmG6WRvibuKCe5IeZJ21RoIeCsdp3ho/f/+QQLlnqaa1dw6i4xMFM0e2IaxujhQiWnd4by23CkMPvzKhy6YP3wPBq+awpzEPLDZcD8l1i0SqmX7HNpmw4kXBrWzEimAzp1aqONVau4kIwCGwJFusMdErw9IL7KQ5VqMKN4Xl67s0pwotoXsA+5SlWQAIodipYKZnPzwO";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    String label = "NONE";
    boolean marker = false;


    public void runOpMode() {
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.5, 16.0/9.0);
        }

        waitForStart();

        while(opModeIsActive()){
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

                /*if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    boolean marker = false;
                    for (Recognition recognition : updatedRecognitions) {
                        //if(recognition.getLabel().equals("Marker")) marker = true;
                        //if(!marker) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            i++;
                            telemetry.addData("Sees", "something");
                        //}
                        //marker = false;
                    }
                    //telemetry.update();
                }

                 */


                /*if(updatedRecognitions != null){
                    if(updatedRecognitions.size() != 0){
                        for (Recognition recognition : updatedRecognitions) {
                            if(recognition.getLeft() > 255){
                                telemetry.addData("Duck position", "Right");
                            }
                            else if(recognition.getLeft() <= 255){
                                telemetry.addData("Duck position", "Middle");
                            }
                        }
                    }
                    else{
                        telemetry.addData("Duck position", "Left");
                    }
                }
                telemetry.update();

                 */

                if(updatedRecognitions != null){
                    if(updatedRecognitions.size() != 0){
                        for(Recognition recognition : updatedRecognitions) {
                            telemetry.addData("Label: ", recognition.getLabel());
                            if(recognition.getLabel().equals("Marker")) {
                                marker = true;
                            }else{
                                marker = false;
                            }
                            if(!marker) {
                                if (recognition.getLeft() < 135) {
                                    label = "Left";
                                } else if (recognition.getLeft() >= 135 && recognition.getLeft() <= 386) {
                                    label = "Middle";
                                } else if (recognition.getLeft() > 386) {
                                    label = "Right";
                                }
                            }
                            telemetry.addData("Left cord: ", recognition.getLeft());
                        }
                    }
                    else{
                        label = "None";
                    }
                }
                telemetry.addData("Position: ", label);
                telemetry.addData("Marker: ", marker);
                telemetry.update();
            }
        }
    }

    private void initVuforia() {

         //Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "camera");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.3f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 800;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}

