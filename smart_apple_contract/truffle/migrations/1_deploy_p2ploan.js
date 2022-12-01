const P2pLoan = artifacts.require("P2pLoan");

module.exports = async function (deployer) {
  await deploy(deployer);
  console.log('deploy done')
};

async function deploy(deployer){
	const accounts = await web3.eth.getAccounts();

	console.log('Attempting to deploy from account', accounts[0]);
	let balance = await web3.eth.getBalance(accounts[0])
	console.log('balance = ',balance);
	await deployer.deploy(P2pLoan,{from: accounts[0]});
	console.log('deployer done ');
}
