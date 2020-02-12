const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Sends a notifications to all users when a new message is posted.
exports.sendNotifications = functions.firestore.document('recipes/{id}').onCreate(
  async (snapshot) => {
    // Notification details.
    const text = snapshot.data().recipe.label;
    const payload = {
      notification: {
        title: `${snapshot.data().recipe.label} posted ${text ? 'a message' : 'an image'}`,
        body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
        //icon: snapshot.data().profilePicUrl || '/images/profile_placeholder.png',
       // click_action: `https://${process.env.GCLOUD_PROJECT}.firebaseapp.com`,
      }
    };


    // Get the list of device tokens.
    const allTokens = await admin.firestore().collection('fcmTokens').get();
    const tokens = [];
    allTokens.forEach((tokenDoc) => {
      tokens.push(tokenDoc.id);
    });

    if (tokens.length > 0) {
      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokens, payload);
      await cleanupTokens(response, tokens);
      console.log('Notifications have been sent and tokens cleaned up.');
    }
  });

/*exports.myStorageFunction = functions
    .region('europe-west1')
    .storage
    .object()
    .onFinalize((object) => {
      // ...
    });*/
/*
exports.insertRecipeReq = functions.firestore
  .document('recipes/{documentId}')
  .onCreate(event => {

	var recipesInfo = event.data.val();
	var recipeName = recipesInfo.recipe.label;
	var userInfo = recipesInfo.userId;

        console.log(`selected recipe ${recipeName}`);
        console.log(`user id ${userInfo}`);

	var recipeRef = admin.firestore().collection('recipes')

	var userName;
	recipeRef.where('userId', '==', userInfo).get()
    		.then(snapshot => {
		  userName = snapshot.username;
		  console.log(`no of loan requests ${snapshot.username}`);
    	})
    	.catch(err => {
           console.log('Error recipe owner', err);
       });
});*/

/*
exports.insertRecipeReq = functions.firestore
    .document('recipes/{id}')
    .onCreate((snap, context) => {
      // Get an object representing the document
      // e.g. {'name': 'Marie', 'age': 66}
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
                  body: " Come to EAT"
                 // sound: "default"
              },
          };

        //Create an options object that contains the time to live for the notification and the priority
          const options = {
              priority: "high"
             */
/* ,
              timeToLive: 60 * 60 * 24*//*

          };


          return admin.messaging().sendToTopic("pushNotifications", payload, options);
    });
*/
