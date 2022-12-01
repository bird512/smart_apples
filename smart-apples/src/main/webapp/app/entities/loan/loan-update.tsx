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
import { CURRENCY } from 'app/shared/model/enumerations/currency.model';
import { LoanStatus } from 'app/shared/model/enumerations/loan-status.model';
import { getEntity, updateEntity, createEntity, reset } from './loan.reducer';
import { makePayment, registerLoan, takeLoan } from 'app/contract/web3';
import { getEntities } from '../address/address.reducer';

export const LoanUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const loanEntity = useAppSelector(state => state.loan.entity);
  const loading = useAppSelector(state => state.loan.loading);
  const updating = useAppSelector(state => state.loan.updating);
  const updateSuccess = useAppSelector(state => state.loan.updateSuccess);
  const account = useAppSelector(state => state.authentication.account);
  const addressList = useAppSelector(state => state.address.entities);
  const cURRENCYValues = Object.keys(CURRENCY);
  const loanStatusValues = Object.keys(LoanStatus);

  const handleClose = () => {
    navigate('/loan');
  };
  console.log(addressList, 'addreddList=======');

  useEffect(() => {
    console.log('------------');
    // takeLoan(1,2000);
    // makePayment(1);
    // registerLoan(200000, 4, 7).then(r  =>{
    //   console.log(' rs = ',r);
    // });
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);
  const [addressMap, setAddressMap] = useState({});
  useEffect(() => {
    dispatch(getEntities({})).then(res => {});
  }, []);
  useEffect(() => {
    let obj = {};
    addressList.forEach(element => {
      obj[element.id] = element.address;
    });
    setAddressMap(obj);
  }, [addressList]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    // values.createAt = convertDateTimeToServer(values.createAt);
    // values.updateAt = convertDateTimeToServer(values.updateAt);

    const entity = {
      ...loanEntity,
      ...values,
      // owner: users.find(it => it.id.toString() === values.owner.toString()),
    };

    if (isNew) {
      registerLoan(entity.loanAmt, entity.interestRate, entity.terms);
      // dispatch(createEntity(entity));
    } else {
      // dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createAt: displayDefaultDateTime(),
          updateAt: displayDefaultDateTime(),
        }
      : {
          currency: 'USD',
          status: 'PENDING',
          ...loanEntity,
          createAt: convertDateTimeFromServer(loanEntity.createAt),
          updateAt: convertDateTimeFromServer(loanEntity.updateAt),
          owner: loanEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartAppleApp.loan.home.createOrEditLabel" data-cy="LoanCreateUpdateHeading">
            <Translate contentKey="smartAppleApp.loan.home.createOrEditLabel">Create or edit a Loan</Translate>
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
                  id="loan-id"
                  label={translate('global.field.id')}
                  disabled
                  validate={{ required: true }}
                />
              ) : null}
              {/* <ValidatedField
                label={translate('smartAppleApp.loan.availableAmt')}
                id="loan-availableAmt"
                name="availableAmt"
                data-cy="availableAmt"
                type="text"
                disabled
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              /> */}
              {/* <ValidatedField
                label={translate('smartAppleApp.loan.hash')}
                id="loan-hash"
                name="hash"
                data-cy="hash"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              /> */}
              <ValidatedField
                label={translate('smartAppleApp.loan.loanAmt')}
                id="loan-loanAmt"
                name="loanAmt"
                data-cy="loanAmt"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 10000, message: translate('entity.validation.min', { min: 10000 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={`${translate('smartAppleApp.loan.interestRate')}(%)`}
                id="loan-interestRate"
                name="interestRate"
                data-cy="interestRate"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('smartAppleApp.loan.terms')}
                id="loan-terms"
                name="terms"
                data-cy="terms"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 2, message: translate('entity.validation.min', { min: 2 }) },
                  max: { value: 12, message: translate('entity.validation.max', { max: 12 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              {/* <ValidatedField
                label={translate('smartAppleApp.loan.createAt')}
                id="loan-createAt"
                name="createAt"
                data-cy="createAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartAppleApp.loan.updateAt')}
                id="loan-updateAt"
                name="updateAt"
                data-cy="updateAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              {/* <ValidatedField
                label={translate('smartAppleApp.loan.currency')}
                id="loan-currency"
                name="currency"
                data-cy="currency"
                type="select"
              >
                {cURRENCYValues.map(cURRENCY => (
                  <option value={cURRENCY} key={cURRENCY}>
                    {translate('smartAppleApp.CURRENCY.' + cURRENCY)}
                  </option>
                ))}
              </ValidatedField> */}
              {/* <ValidatedField label={translate('smartAppleApp.loan.status')} id="loan-status" name="status" data-cy="status" type="select">
                {loanStatusValues.map(loanStatus => (
                  <option value={loanStatus} key={loanStatus}>
                    {translate('smartAppleApp.LoanStatus.' + loanStatus)}
                  </option>
                ))}
              </ValidatedField> */}
              {/* <ValidatedField id="loan-owner" name="owner" data-cy="owner" label={translate('smartAppleApp.loan.owner')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/loan" replace color="info">
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

export default LoanUpdate;
