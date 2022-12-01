const P2pLoan = artifacts.require("P2pLoan");

contract('P2pLoan', () => {
  it('should read newly written values', async() => {
    const loanInstance = await P2pLoan.deployed();
    await loanInstance.registerLoan.call(1000,1,1);
    // console.log('register1 = ',register1);
    await loanInstance.registerLoan(1000,1,1);
    var total = (await loanInstance.total.call()).toNumber();
    assert.equal(total, 1, "registerLoan fail");
    await loanInstance.registerLoan(12000,1,1);
    var total2 = (await loanInstance.total.call()).toNumber();
    assert.equal(total2, 2, "registerLoan fail");

    var getValue = (await loanInstance.getLoans.call());
    console.log('getValue = ',getValue);

    await loanInstance.takeLoan(0,200);

  });
});
