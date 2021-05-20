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

var orders = [
    {
        id: 1,
        customer_id: "123456",
        status: "In Progress",
        ordered_at: "2021-05-07 08:50:53",
        takeaway: false,
        payment_type: "cash",
        delivery_address: {
            street: "something",
            city: "something",
            country: "something",
            zipcode: "something" 
        },
        pizzas: [
            
                {
                    pizza_id: 0,
                    name: "margarita",
                    vegetarian: true,
                    price: 12.50
                },
                {
                    pizza_id: 1,
                    name: "meatlovers",
                    vegetarian: false,
                    price: 15.75
                }
        ]
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

app.get('/order/:id', (req, res) => {
    const id = filterInt(req.params.id);
    if(isNaN(id)) res.status(400).send('Invalid ID supplied');
    const order = orders.find(c => c.id === id);
    if(!order) res.status(404).send('Order ID not found');
    res.status(200).send(order);
});

/*app.get('/pizza', (req, res) => {
    res.status(200).send(pizzas);
});

app.get('/pizza', (req, res) => {
    res.status(200).send(pizzas);
});

app.get('/pizza', (req, res) => {
    res.status(200).send(pizzas);
});

app.get('/pizza', (req, res) => {
    res.status(200).send(pizzas);
});*/

//function to filter integers from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/parseInt 
function filterInt(value) {
    if (/^[-+]?(\d+|Infinity)$/.test(value)) {
      return Number(value)
    } else {
      return NaN
    }
  }

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));