const express = require('express');
const app = express();

const pizza = require('./routes/pizza');
const order = require('./routes/order');
app.get('/', (req, res) => {
    res.send('Hello World');
});
app.use('/pizza', pizza);
app.use('/order',order)
const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));