const HDWalletProvider = require('@truffle/hdwallet-provider');
var mnemonic = "bacon change daughter response suffer like argue cloth wealth foot coach same";

module.exports = {
	 networks: {
		   development: {
			      host: "127.0.0.1",
			      port: 8545,
			      network_id: "*"
			     },
		   goerli: {
			         provider: function() { 
					        return new HDWalletProvider(mnemonic, "https://goerli.infura.io/v3/995c4ecd1360431880b4b0a29d11be94");
					       },
			         network_id: 5,
			         //gas: 4500000,
			         timeoutBlocks: 200, 
			     }
		  },
	compilers: {
		    solc: {
			          version: "0.8.16",
			        }
		  },
};
