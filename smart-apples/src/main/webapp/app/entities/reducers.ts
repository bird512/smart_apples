import loan from 'app/entities/loan/loan.reducer';
import loanSubscription from 'app/entities/loan-subscription/loan-subscription.reducer';
import payment from 'app/entities/payment/payment.reducer';
import message from 'app/entities/message/message.reducer';
import address from 'app/entities/address/address.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  loan,
  loanSubscription,
  payment,
  message,
  address,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
