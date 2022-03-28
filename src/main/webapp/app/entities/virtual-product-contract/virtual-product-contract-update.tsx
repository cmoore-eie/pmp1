import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IContract } from 'app/shared/model/contract.model';
import { getEntities as getContracts } from 'app/entities/contract/contract.reducer';
import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { getEntities as getVirtualProducts } from 'app/entities/virtual-product/virtual-product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './virtual-product-contract.reducer';
import { IVirtualProductContract } from 'app/shared/model/virtual-product-contract.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductContractUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const contracts = useAppSelector(state => state.contract.entities);
  const virtualProducts = useAppSelector(state => state.virtualProduct.entities);
  const virtualProductContractEntity = useAppSelector(state => state.virtualProductContract.entity);
  const loading = useAppSelector(state => state.virtualProductContract.loading);
  const updating = useAppSelector(state => state.virtualProductContract.updating);
  const updateSuccess = useAppSelector(state => state.virtualProductContract.updateSuccess);
  const handleClose = () => {
    props.history.push('/virtual-product-contract');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getContracts({}));
    dispatch(getVirtualProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...virtualProductContractEntity,
      ...values,
      contract: contracts.find(it => it.id.toString() === values.contract.toString()),
      virtualProduct: virtualProducts.find(it => it.id.toString() === values.virtualProduct.toString()),
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
          ...virtualProductContractEntity,
          contract: virtualProductContractEntity?.contract?.id,
          virtualProduct: virtualProductContractEntity?.virtualProduct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.virtualProductContract.home.createOrEditLabel" data-cy="VirtualProductContractCreateUpdateHeading">
            Create or edit a VirtualProductContract
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
                <ValidatedField name="id" required readOnly id="virtual-product-contract-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Ancestor Item Identifier"
                id="virtual-product-contract-ancestorItemIdentifier"
                name="ancestorItemIdentifier"
                data-cy="ancestorItemIdentifier"
                type="text"
              />
              <ValidatedField
                label="Item Identifier"
                id="virtual-product-contract-itemIdentifier"
                name="itemIdentifier"
                data-cy="itemIdentifier"
                type="text"
              />
              <ValidatedField label="Priority" id="virtual-product-contract-priority" name="priority" data-cy="priority" type="text" />
              <ValidatedField id="virtual-product-contract-contract" name="contract" data-cy="contract" label="Contract" type="select">
                <option value="" key="0" />
                {contracts
                  ? contracts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="virtual-product-contract-virtualProduct"
                name="virtualProduct"
                data-cy="virtualProduct"
                label="Virtual Product"
                type="select"
                required
              >
                <option value="" key="0" />
                {virtualProducts
                  ? virtualProducts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/virtual-product-contract" replace color="info">
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

export default VirtualProductContractUpdate;
