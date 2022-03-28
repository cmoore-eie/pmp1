import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './agreement.reducer';
import { IAgreement } from 'app/shared/model/agreement.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { AgreementCancelReason } from 'app/shared/model/enumerations/agreement-cancel-reason.model';

export const AgreementUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const agreementEntity = useAppSelector(state => state.agreement.entity);
  const loading = useAppSelector(state => state.agreement.loading);
  const updating = useAppSelector(state => state.agreement.updating);
  const updateSuccess = useAppSelector(state => state.agreement.updateSuccess);
  const agreementCancelReasonValues = Object.keys(AgreementCancelReason);
  const handleClose = () => {
    props.history.push('/agreement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...agreementEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          cancelReason: 'CANCEL_BY_CARRIER',
          ...agreementEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.agreement.home.createOrEditLabel" data-cy="AgreementCreateUpdateHeading">
            Create or edit a Agreement
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="agreement-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Cancel Reason" id="agreement-cancelReason" name="cancelReason" data-cy="cancelReason" type="select">
                {agreementCancelReasonValues.map(agreementCancelReason => (
                  <option value={agreementCancelReason} key={agreementCancelReason}>
                    {agreementCancelReason}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/agreement" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AgreementUpdate;
