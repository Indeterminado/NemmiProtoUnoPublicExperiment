
# NemmiProtoUno Experiment Resources

In this section you can find all resources need to implement your own instance of the experiment.

  

For specific description of the structure of the experiment, I would urge you to read the paper describing it (see project root), as you will need to be aware of the rationale behind it to understand its structure. After you do, these files and their objective should make sense.

  

In terms of hardware, you will need:

  

- [ ] one handheld device (smartphone or tablet) running Android

- [ ] the provided app installed in said device, with appropriate permissions already granted

- [ ] one computer with Pure Data installed

- [ ] good quality speakers

- [ ] one device to record video and audio (e.g. smartphone, camera), ideally mounted on a tripod

  

## Specific instructions

### Mobile app

The app should be running on the device you hand participants, so download the source code provided, compile it and make sure it runs on the device. The device should be connected to the same network as the computer running the PD patch.

### Pure Data control patch

This is how you will control the flow of the experiment. From this patch you will be triggering the sounds for the participant to listen and control the mobile app. The only thing you need to change is the IP to which the patch will send messages. After the device with the app is connected to the network, check its IP address and change it in the PD patch before connecting. You can send a *soundoff* message from it to check if communication is working.

### Full experiment script

Just as the name says, this is the full script you should follow for the experiment. Much like a movie script, all moments are detailed, and you should follow it - of course specific discourse can be adapted.

### Participant details template

This is an SPSS we provide, pre-formatted for input just as we used, and ready to run the syntax file in [Results and Analysis](https://github.com/Indeterminado/NemmiProtoUnoPublicExperiment/tree/main/Results%20and%20Analysis  "Results and Analysis"). The easiest way would be to first write down participant answers on an Excel Spreadsheet or Word Document and then convert it to this format.

Important things to know: 

1. Gesture list is based on our own instance of the experiment and may not cover all gestures you encounter. In that case you should edit the *Values* list for all corresponding mapping variables (*Variable View* tab has descriptions for that - check for the *uncategorized mappings* variables)

2. Same goes for participant rationale. In the paper we describe the categorization we implemented based on the answers we collected, but you are free to expand on this (check *Variable View* tab for *Rationale* variables and update list)

  

*Variable View* descriptions should explain each variable's objective.
For each participant you will have to fill in:

 - ID (this is just a numeric identifier)
 - Profile (musician VS non-musician)
 - Age
 - Gender
 - Attributed order for first three stimuli (read the paper)
 - Details for each of the stimuli
	 - stimuli perception (X_PAR_PHASE)
	 - gesture mapping (X_MAP_PHASE_L1)
	 - gesture categorization (X_MAP_PHASE_L2)
	 - mapping rationale (X_RSN_PHASE)

X represents the stimulus id (a-e), PAR represents the musical parameter of that stimulus (PITch, DURation, AMPlitude) - L1 is the uncategorized gesture, L2 is the broad category of the gesture (read the paper), RSN means reason, and PHASE represents the phase number of the data (read the... ok, you get it)
Stimulus D is the only one with a different variable structure, with the gesture variables following a format of (D_PARMAP_PHASE_L1). PAR follows the same logic as other stimuli.

  

## Putting it all together

If you go over the Full experiment script after reading the paper, you should be set to run the experiment yourself. If any doubts persist, or if explanation is too confusing, feel free to drop me a message.

  

Alexandre Clément
