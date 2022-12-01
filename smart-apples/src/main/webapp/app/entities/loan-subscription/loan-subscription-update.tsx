import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ILoan } from 'app/shared/model/loan.model';
import { getEntities as getLoans } from 'app/entities/loan/loan.reducer';
import { ILoanSubscription } from 'app/shared/model/loan-subscription.model';
import { getEntity, updateEntity, createEntity, reset } from './loan-subscription.reducer';
import { getEntity as getLoanEntity } from '../loan/loan.reducer';
import { makePayment, registerLoan, takeLoan } from 'app/contract/web3';

export const LoanSubscriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { loanId } = useParams<'loanId'>();
  const isNew = id === undefined;

  useEffect(() => {
    dispatch(getLoanEntity(loanId));
  }, []);

  const loanEntity = useAppSelector(state => state.loan.entity);
  const users = useAppSelector(state => state.userManagement.users);
  const account = useAppSelector(state => state.authentication.account);
  const loans = useAppSelector(state => state.loan.entities);
  const loanSubscriptionEntity = useAppSelector(state => state.loanSubscription.entity);
  const loading = useAppSelector(state => state.loanSubscription.loading);
  const updating = useAppSelector(state => state.loanSubscription.updating);
  const updateSuccess = useAppSelector(state => state.loanSubscription.updateSuccess);
  const handleClose = () => {
    navigate('/loan-subscription');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getLoans({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    // values.createAt = convertDateTimeToServer(values.createAt);
    // values.updateAt = convertDateTimeToServer(values.updateAt);

    const entity = {
      ...loanSubscriptionEntity,
      ...values,
      subscriber: users.find(it => it.id.toString() === values.subscriber.toString()),
      loan: loans.find(it => it.id.toString() === values.loan.toString()),
    };

    if (isNew) {
      takeLoan(loanId, entity.subAmt);
      // dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createAt: displayDefaultDateTime(),
          updateAt: displayDefaultDateTime(),
          loan: loanId,
          subAmt: loanEntity.availableAmt,
          subscriber: account.id,
        }
      : {
          ...loanSubscriptionEntity,
          createAt: convertDateTimeFromServer(loanSubscriptionEntity.createAt),
          updateAt: convertDateTimeFromServer(loanSubscriptionEntity.updateAt),
          subscriber: loanSubscriptionEntity?.subscriber?.id || account.id,
          loan: loanId ? loanId : loanSubscriptionEntity?.loan?.id,
          subAmt: loanSubscriptionEntity && loanSubscriptionEntity.subAmt ? loanSubscriptionEntity.subAmt : loanEntity.availableAmt,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartAppleApp.loanSubscription.home.createOrEditLabel" data-cy="LoanSubscriptionCreateUpdateHeading">
            <Translate contentKey="smartAppleApp.loanSubscription.home.createOrEditLabel">Create or edit a LoanSubscription</Translate>
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
                  id="loan-subscription-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              {/* <ValidatedField
                label={translate('smartAppleApp.loanSubscription.hash')}
                id="loan-subscription-hash"
                name="hash"
                data-cy="hash"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              /> */}
              <ValidatedField
                label={translate('smartAppleApp.loanSubscription.subAmt')}
                id="loan-subscription-subAmt"
                name="subAmt"
                data-cy="subAmt"
                disabled
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              {/* <ValidatedField
                label={translate('smartAppleApp.loanSubscription.createAt')}
                id="loan-subscription-createAt"
                name="createAt"
                data-cy="createAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartAppleApp.loanSubscription.updateAt')}
                id="loan-subscription-updateAt"
                name="updateAt"
                data-cy="updateAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              <ValidatedField
                id="loan-subscription-subscriber"
                name="subscriber"
                data-cy="subscriber"
                disabled
                label={translate('smartAppleApp.loanSubscription.subscriber')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="loan-subscription-loan"
                name="loan"
                data-cy="loan"
                disabled={loanId !== null}
                label={translate('smartAppleApp.loanSubscription.loan')}
                type="select"
              >
                <option value="" key="0" />
                {loans
                  ? loans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/loan-subscription" replace color="info">
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

export default LoanSubscriptionUpdate;
