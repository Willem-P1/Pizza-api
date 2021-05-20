//place to handle pizzas

const express = require('express');
const router = express.Router();

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

router.get('/', (req, res) => {
    res.status(200).send(pizzas);
});

router.get('/:id', (req, res) => {
    const pizza = pizzas.find(c => c.pizza_id === parseInt(req.params.id));
    if (!pizza) res.status(404).send('Pizza not found');
    res.status(200).send(pizza);
});

module.exports = router;