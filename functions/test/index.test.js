const test = require('firebase-functions-test')();
const myFunctions = require('../index.js');
const wrapped = test.wrap(myFunctions.insertRecipeReq);

const data = … // See next section for constructing test data

// Invoke the wrapped function without specifying the event context.
wrapped(data);

// Invoke the function, and specify params
wrapped(data, {
  params: {
    pushId: '234234'
  }
});