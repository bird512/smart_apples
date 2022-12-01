import Web3 from 'web3';
import { toast } from 'react-toastify';
//bacon change daughter response suffer like argue cloth wealth foot coach same
//apple123
/* eslint no-console: off */

let contract;
let account;
const contractAddr = '0x3Fb8dbe0f53568361f9b4Bcda2815DE072aa992f';

export async function connect() {
  const web3 = new Web3(Web3.givenProvider);
  const accounts = await web3.eth.requestAccounts();
  const networkID = await web3.eth.net.getId();
  if (networkID != 5) {
    // alert('please install metamask and  select the network to goerli');
    toast.error('please install metamask and  select the network to goerli');
    return;
  }
  account = accounts[0];
  const artifact = require('./P2pLoan.json');
  const { abi } = artifact;
  try {
    contract = new web3.eth.Contract(abi, contractAddr);
    console.log('contract = ', contract, abi);
  } catch (err) {
    console.error(err);
  }
}

export async function registerLoan(amount, rate, terms) {
  if (!account) {
    await connect();
  }

  // return await contract.methods.registerLoan(amount, rate, terms).send({ from: account });

  return contract.methods.registerLoan(amount, rate, terms).send({ from: account }, (err, rs) => {
    console.log('rs = ', rs);
    if (err) {
      toast.error(err.message || 'Unknown error!');
    } else {
      toast.success('Transactioin send success: ' + rs);
    }
    console.log('registerLoan ', err);
  });
}
export async function takeLoan(loanId, amount) {
  if (!account) {
    await connect();
  }
  return contract.methods.takeLoan(loanId).send({ from: account }, (err, rs) => {
    if (err) {
      toast.error(err.message || 'Unknown error!');
    } else {
      toast.success('Transactioin send success: ' + rs);
    }
  });
}
export async function makePayment(loanId) {
  if (!account) {
    await connect();
  }
  return contract.methods.makePayment(loanId).send({ from: account }, (err, rs) => {
    if (err) {
      toast.error(err.message || 'Unknown error!');
    } else {
      toast.success('Transactioin send success: ' + rs);
    }
  });
}
