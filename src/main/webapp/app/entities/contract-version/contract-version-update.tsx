import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IContract } from 'app/shared/model/contract.model';
import { getEntities as getContracts } from 'app/entities/contract/contract.reducer';
import { getEntity, updateEntity, createEntity, reset } from './contract-version.reducer';
import { IContractVersion } from 'app/shared/model/contract-version.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ContractVersionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const contracts = useAppSelector(state => state.contract.entities);
  const contractVersionEntity = useAppSelector(state => state.contractVersion.entity);
  const loading = useAppSelector(state => state.contractVersion.loading);
  const updating = useAppSelector(state => state.contractVersion.updating);
  const updateSuccess = useAppSelector(state => state.contractVersion.updateSuccess);
  const handleClose = () => {
    props.history.push('/contract-version');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getContracts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...contractVersionEntity,
      ...values,
      contract: contracts.find(it => it.id.toString() === values.contract.toString()),
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
          ...contractVersionEntity,
          contract: contractVersionEntity?.contract?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.contractVersion.home.createOrEditLabel" data-cy="ContractVersionCreateUpdateHeading">
            Create or edit a ContractVersion
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
                <ValidatedField name="id" required readOnly id="contract-version-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Effective Date"
                id="contract-version-effectiveDate"
                name="effectiveDate"
                data-cy="effectiveDate"
                type="date"
              />
              <ValidatedField
                label="Expiration Date"
                id="contract-version-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="date"
              />
              <ValidatedField
                label="Hidden Contract"
                id="contract-version-hiddenContract"
                name="hiddenContract"
                data-cy="hiddenContract"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Version Number"
                id="contract-version-versionNumber"
                name="versionNumber"
                data-cy="versionNumber"
                type="text"
              />
              <ValidatedField id="contract-version-contract" name="contract" data-cy="contract" label="Contract" type="select" required>
                <option value="" key="0" />
                {contracts
                  ? contracts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contract-version" replace color="info">
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

export default ContractVersionUpdate;
