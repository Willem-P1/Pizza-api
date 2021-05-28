const express = require('express');
const app = express();

app.use(express.json());

const pizzas = [
    {   pizza_id: 0, 
        name: "margarita", 
        vegetarian: true, 
        price: 12.50,
        toppings: [
            "Mozzarella",
            "Tomatoes",
            "Basil"
        ]
    },
    {   pizza_id: 1, 
        name: "meatlovers", 
        vegetaria: false, 
        price: 15.75,
        toppings: [
            "Parmesan cheese",
            "Pepperoni",
            "Ham",
            "Hot sausage",
            "Tomatoes"
        ]
    }
];

var orders = [
    {
        id: 1,
        customer_id: "123456",
        status: "In Progress",
        ordered_at: "2021-05-22T23:17:53.511Z",
        takeaway: false,
        payment_type: "cash",
        delivery_address: {
            street: "Paul-Henri Spaaklaan 1",
            city: "Maastricht",
            country: "Netherlands",
            zipcode: "6229 EN" 
        },
        pizzas: [
            0,
            0,
            1
        ],
        note: "No Onions"
    }
];

app.get('/api/v1/', (req, res) => {
    res.send('Pizza API');
});

app.get('/api/v1/pizza', (req, res) => {
    res.status(200).send(pizzas);
});

app.get('/api/v1/pizza/:id', (req, res) => {
    const pizza = pizzas.find(c => c.pizza_id === parseInt(req.params.id));
    if (!pizza) {
        res.status(404).send('Pizza not found');
        return;
    } 
    res.status(200).send(pizza);
});

app.get('/api/v1/order/:id', (req, res) => {
    const id = filterInt(req.params.id);
    if(isNaN(id)) {
        res.status(400).send('Invalid ID supplied');
        return;
    }
    const order = orders.find(c => c.id === id);
    if(!order) {
        res.status(404).send('Order ID not found');
        return;
    } 
    res.status(200).send(order);
});

app.get('/api/v1/order/deliverytime/:id', (req, res) => {
    const order = orders.find(c => c.id === parseInt(req.params.id));
    if (!order) {
        res.status(404).send('Order not found');
        return;
    } 
    order.delivery_time = calculateDeliveryTime(order.ordered_at, 30);
    res.status(200).send(order);
});

app.post('/api/v1/order', (req, res) => {
    if (!req.body.pizzas || !req.body.takeaway || !req.body.payment_type || !req.body.customer_id || !req.body.delivery_address) {
        res.status(400).send("The format of the object is not valid");
        return;
    } 
    const order = {
        id: orders.length + 1,
        customer_id: req.body.customer_id,
        status: "In Progress",
        ordered_at: getTime(),
        takeaway: req.body.takeaway,
        payment_type: req.body.payment_type,
        delivery_address: req.body.delivery_address,
        pizzas: req.body.pizzas,
        note: req.body.note
    };
    orders.push(order);
    order.delivery_time = calculateDeliveryTime(order.ordered_at, 30);
    res.status(200).send(order);
});

app.put('/api/v1/order/cancel/:id', (req, res) => {
    const order = orders.find(c => c.id === parseInt(req.params.id));
    if (!order) {
        res.status(404).send("Order not found");
        return;
    } 
    if (order.status === "cancelled" || order.status === "delivered") {
        res.status(422).send("Unable to cancel an already canceled or delivered order");
        return;
    } 
    if (check5MinutesPassed(order.ordered_at)) {
        res.status(412).send("Unable to cancel your order after 5 minutes have elapsed");
        return;
    }
    order.status = "canceled";
    const returnOrder = {
        order_id: order.id,
        status: "Cancelled"
    };
    res.status(200).send(returnOrder);
});



//function to filter integers from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/parseInt 
function filterInt(value) {
    if (/^[-+]?(\d+|Infinity)$/.test(value)) {
      return Number(value)
    } else {
      return NaN
    }
}

function getTime() {
    var date = (new Date()).getTimezoneOffset() * 60000;
    return (new Date(Date.now() - date)).toISOString();
}

function check5MinutesPassed(orderTimeString) {
    var orderTime = new Date(orderTimeString).getTime();
    if (new Date().getTime() - orderTime > 300000) {
        return true;
    }
    return false;
}

function calculateDeliveryTime(orderTimeString, minutes) {
    var orderTime = new Date(orderTimeString).getTime();
    return new Date(orderTime + minutes*60000);
}

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));
