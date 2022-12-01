import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILoanSubscription } from 'app/shared/model/loan-subscription.model';
import { getEntities as getLoanSubscriptions } from 'app/entities/loan-subscription/loan-subscription.reducer';
import { IPayment } from 'app/shared/model/payment.model';
import { getEntity, updateEntity, createEntity, reset } from './payment.reducer';
import { getEntity as getLoanEntity } from '../loan/loan.reducer';
import { makePayment, registerLoan, takeLoan } from 'app/contract/web3';

export const PaymentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { loanId } = useParams<'loanId'>();
  const isNew = id === undefined;

  const loanSubscriptions = useAppSelector(state => state.loanSubscription.entities);
  const paymentEntity = useAppSelector(state => state.payment.entity);
  const loading = useAppSelector(state => state.payment.loading);
  const updating = useAppSelector(state => state.payment.updating);
  const updateSuccess = useAppSelector(state => state.payment.updateSuccess);

  const handleClose = () => {
    navigate('/payment');
  };

  const loanEntity = useAppSelector(state => state.loan.entity);
  console.log(loanEntity, 'loanEntity=====');

  useEffect(() => {
    dispatch(getLoanEntity(loanId));
  }, []);

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLoanSubscriptions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    // values.createAt = convertDateTimeToServer(values.createAt);
    // values.updateAt = convertDateTimeToServer(values.updateAt);

    // const entity = {
    //   ...paymentEntity,
    //   ...values,
    //   subscription: loanSubscriptions.find(it => it.id.toString() === values.subscription.toString()),
    // };

    if (isNew) {
      // dispatch(createEntity(entity));
      makePayment(loanId);
    } else {
      // dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createAt: displayDefaultDateTime(),
          updateAt: displayDefaultDateTime(),
          interest: loanEntity.interestRate,
          loanAmt: loanEntity.loanAmt,
        }
      : {
          ...paymentEntity,
          createAt: convertDateTimeFromServer(paymentEntity.createAt),
          updateAt: convertDateTimeFromServer(paymentEntity.updateAt),
          subscription: paymentEntity?.subscription?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartAppleApp.payment.home.createOrEditLabel" data-cy="PaymentCreateUpdateHeading">
            <Translate contentKey="smartAppleApp.payment.home.createOrEditLabel">Create or edit a Payment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="payment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              {/* <ValidatedField label={translate('smartAppleApp.payment.hash')} id="payment-hash" name="hash" data-cy="hash" type="text" /> */}
              <ValidatedField
                label={translate('smartAppleApp.payment.interest')}
                id="payment-interest"
                name="interest"
                data-cy="interest"
                disabled
                type="text"
              />
              <ValidatedField
                label={translate('smartAppleApp.payment.amount')}
                id="payment-principal"
                name="loanAmt"
                data-cy="principal"
                disabled
                type="text"
              />
              {/* <ValidatedField
                label={translate('smartAppleApp.payment.createAt')}
                id="payment-createAt"
                name="createAt"
                data-cy="createAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartAppleApp.payment.updateAt')}
                id="payment-updateAt"
                name="updateAt"
                data-cy="updateAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="payment-subscription"
                name="subscription"
                data-cy="subscription"
                label={translate('smartAppleApp.payment.subscription')}
                type="select"
              >
                <option value="" key="0" />
                {loanSubscriptions
                  ? loanSubscriptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PaymentUpdate;
