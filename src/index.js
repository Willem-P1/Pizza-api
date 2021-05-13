const express = require('express');
const app = express();

const pizzas = [
    {   pizza_id: 0, 
        name: "margarita", 
        vegetarian: true, 
        price: 12.50 
    },
    {   pizza_id: 1, 
        name: "meatlovers", 
        vegetaria: false, 
        price: 15.75 
    }
];

app.get('/', (req, res) => {
    res.send('Hello World');
});

app.get('/pizza', (req, res) => {
    res.status(200).send(pizzas);
});

app.get('/pizza/:id', (req, res) => {
    const pizza = pizzas.find(c => c.pizza_id === parseInt(req.params.id));
    if (!pizza) res.status(404).send('Pizza not found');
    res.status(200).send(pizza);
});


const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));