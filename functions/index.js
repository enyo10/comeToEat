const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.myStorageFunction = functions
    .region('europe-west1')
    .storage
    .object()
    .onFinalize((object) => {
      // ...
    });

exports.insertRecipeReq = functions.firestore
    .document('recipes/{id}')
    .onCreate((snap, context) => {
      // Get an object representing the document

      const newValue = snap.data();

      // access a particular field as you would any JS property
      const userInfo = newValue.id;

       console.log(`selected recipe ${newValue}`);
              console.log(`user id ${userInfo}`);

      // perform desired operations ...

      // Create a notification
          const payload = {
              notification: {
                  title:"Invitation",
                  body: " Come to EAT : " + newValue.label
                //  sound: "default"
              },
          };

        //Create an options object that contains the time to live for the notification and the priority
          const options = {
              priority: "high"
              ,
              timeToLive: 60

          };


          return admin.messaging().sendToTopic("pushNotifications", payload, options);
    });
