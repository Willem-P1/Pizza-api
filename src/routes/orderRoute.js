//place to handle orders

const express = require('express');
const router = express.Router();

router.get('/deliverytime/:id', (req, res) => {
    res.status(200).send("Hello world");
});

module.exports = router;