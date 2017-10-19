var bitcoin = require('./src/index');

//P2SH
//const privateKey = bitcoin.ECPair.fromWIF('Kyb4EiNEwxeSKtmHEmBfh1wyRzcuX2m3V8BDLHqixkZgjmZG1oax');

var privKeys = [
    'L4fzjKNuSgxpgSKDsy2qtxre8uXhKqgkMsjeXg6NTDzKbdMx4EK1', // #1
    'L1qGx6pbi5Vcp3Yj2drnhZ2QmSTea4P5JFgLLuxuKjCNVpB4nngW'  // #2
];

// addr = '39J9tCAb3KgNiRWdfnfr21v74AYma7uu65'

var redeemScript = new Buffer('522102c02ddd786a704f634beb7b9e4f973eb8cb4c35fb2e212b30c1ec27188d840a9121025d76f620b67462dc33aa36820867c2380f720495771a9c4aa31bfb25b0ab20e652ae','hex');

var tx = bitcoin.Transaction.fromHex("0100000001d29cccc667f90deb2a5c30e35a4b2099765f2881571599285482dbf28654e8020100000017a914536e1558aefe767a27f6677355db5f9e768620b187ffffffff0200c2eb0b000000001976a914fafa68f4cf68f4d9db7e714c32937182415da7de88ac0100000000778f060200000017a914536e1558aefe767a27f6677355db5f9e768620b187010000000000000000000000");
var txb = bitcoin.TransactionBuilder.fromTransaction(tx);

var rawSignedTransaction;

txb.tx.ins.forEach(function(input, i) {
    txb.sign(i, bitcoin.ECPair.fromWIF(privKeys[0]), redeemScript)
    txb.sign(i, bitcoin.ECPair.fromWIF(privKeys[1]), redeemScript)

    try {
        rawSignedTransaction = txb.build().toHex();
        console.log(rawSignedTransaction);
        console.log('Successfully signed.');
    } catch (e) {
        console.log(e);
    }
});


//P2PKH
// const priv = bitcoin.ECPair.fromWIF('Kyb4EiNEwxeSKtmHEmBfh1wyRzcuX2m3V8BDLHqixkZgjmZG1oax');
// // addr = 'Kyb4EiNEwxeSKtmHEmBfh1wyRzcuX2m3V8BDLHqixkZgjmZG1oax'
//
// var tx = bitcoin.Transaction.fromHex("0100000002ce1acb4e57ec315b1ad6b6ad13289bf2ddf30071c3c17866d111d8507d98ac37010000001976a914fafa68f4cf68f4d9db7e714c32937182415da7de88acffffffffce1acb4e57ec315b1ad6b6ad13289bf2ddf30071c3c17866d111d8507d98ac37000000001976a914fafa68f4cf68f4d9db7e714c32937182415da7de88acffffffff0100c2eb0b000000001976a914fafa68f4cf68f4d9db7e714c32937182415da7de88ac010000000000000000000000");
// var txb = bitcoin.TransactionBuilder.fromTransaction(tx);
// var rawSignedTransaction;
//
// txb.tx.ins.forEach(function(input, i) {
//     txb.sign(i, priv);
//     try {
//         rawSignedTransaction = txb.build().toHex();
//         console.log('Successfully signed.');
//     } catch (e) {
//         console.log(e);
//     }
// });
//
// console.log(rawSignedTransaction);
