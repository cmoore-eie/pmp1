import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './contract.reducer';
import { IContract } from 'app/shared/model/contract.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ItemStatus } from 'app/shared/model/enumerations/item-status.model';

export const ContractUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const contractEntity = useAppSelector(state => state.contract.entity);
  const loading = useAppSelector(state => state.contract.loading);
  const updating = useAppSelector(state => state.contract.updating);
  const updateSuccess = useAppSelector(state => state.contract.updateSuccess);
  const itemStatusValues = Object.keys(ItemStatus);
  const handleClose = () => {
    props.history.push('/contract');
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
      ...contractEntity,
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
          itemStatus: 'DRAFT',
          ...contractEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.contract.home.createOrEditLabel" data-cy="ContractCreateUpdateHeading">
            Create or edit a Contract
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="contract-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Ancestor Item Identifier"
                id="contract-ancestorItemIdentifier"
                name="ancestorItemIdentifier"
                data-cy="ancestorItemIdentifier"
                type="text"
              />
              <ValidatedField label="Code" id="contract-code" name="code" data-cy="code" type="text" />
              <ValidatedField
                label="Item Identifier"
                id="contract-itemIdentifier"
                name="itemIdentifier"
                data-cy="itemIdentifier"
                type="text"
              />
              <ValidatedField label="Item Status" id="contract-itemStatus" name="itemStatus" data-cy="itemStatus" type="select">
                {itemStatusValues.map(itemStatus => (
                  <option value={itemStatus} key={itemStatus}>
                    {itemStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Locked" id="contract-locked" name="locked" data-cy="locked" check type="checkbox" />
              <ValidatedField label="Name" id="contract-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Product Code" id="contract-productCode" name="productCode" data-cy="productCode" type="text" />
              <ValidatedField label="Version Number" id="contract-versionNumber" name="versionNumber" data-cy="versionNumber" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contract" replace color="info">
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

export default ContractUpdate;
