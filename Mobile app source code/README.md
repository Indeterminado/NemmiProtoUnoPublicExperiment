
# NemmiProtoUno Android App

In this section you can find the Android Studio project for the application used in the study of how users map mobile handheld device gestures to musical parameters.

It is a rather simple app, uses no external libraries, and should compile without problems. It implements functionalities for touch and accelerometer reading, as well as network communication (over UDP) and sensor data logging. Just open the **NemiProtoUno** folder with Android Studio and you should be good to go.

  

All code and assets are released under the [GNU AFFERO GENERAL PUBLIC LICENSE](https://opensource.org/licenses/AGPL-3.0), if you use it in any way please respect it. And do let me know, I'm always interested in knowing how it might have been used!

  

## DISCLAIMER
Although I did test this app on many devices, the nature of the Android ecosystem is such that it IS possible that a given device is unable to run this app. No guarantee is made regarding that, but feel free to adapt and correct it.

  

For details on how to use this application in the context of the experiment, check section [Experiment resources](https://github.com/Indeterminado/NemmiProtoUnoPublicExperiment/tree/main/Experiment%20resources  "Experiment resources") of this repository.

### Other info
If you want to access logged data the application creates, you can find log files under the *Android/com.feup.nemiprotouno/files/Nemmi_ProtoUno* directory on your device. 
Logs follow this naming pattern: NemmiProtoUnoLog_DATE_TIME_MILLISECONDS.txt

Events are logged with a timestamp. Event code and values as follows:

- Test ID (when you send the *test start* message for each stimuli this value increments)
- accel X Y Z (accelerometer values on each axis)
- touchStart ID X Y (thouch with ID started at X,Y coordinates)
- move ID X Y (touch with ID moved to X, Y coordinates)
- touchEnd ID X Y (touch with ID ended at X, Y coordinates)
- shake (user shaked the device)
  

Alexandre Clément
