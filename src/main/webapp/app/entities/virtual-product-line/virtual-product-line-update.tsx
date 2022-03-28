import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { getEntities as getVirtualProducts } from 'app/entities/virtual-product/virtual-product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './virtual-product-line.reducer';
import { IVirtualProductLine } from 'app/shared/model/virtual-product-line.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductLineUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const virtualProducts = useAppSelector(state => state.virtualProduct.entities);
  const virtualProductLineEntity = useAppSelector(state => state.virtualProductLine.entity);
  const loading = useAppSelector(state => state.virtualProductLine.loading);
  const updating = useAppSelector(state => state.virtualProductLine.updating);
  const updateSuccess = useAppSelector(state => state.virtualProductLine.updateSuccess);
  const handleClose = () => {
    props.history.push('/virtual-product-line');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getVirtualProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...virtualProductLineEntity,
      ...values,
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
          ...virtualProductLineEntity,
          virtualProduct: virtualProductLineEntity?.virtualProduct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.virtualProductLine.home.createOrEditLabel" data-cy="VirtualProductLineCreateUpdateHeading">
            Create or edit a VirtualProductLine
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
                <ValidatedField name="id" required readOnly id="virtual-product-line-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Ancestor Item Identifier"
                id="virtual-product-line-ancestorItemIdentifier"
                name="ancestorItemIdentifier"
                data-cy="ancestorItemIdentifier"
                type="text"
              />
              <ValidatedField
                label="Item Identifier"
                id="virtual-product-line-itemIdentifier"
                name="itemIdentifier"
                data-cy="itemIdentifier"
                type="text"
              />
              <ValidatedField
                label="Line Available"
                id="virtual-product-line-lineAvailable"
                name="lineAvailable"
                data-cy="lineAvailable"
                check
                type="checkbox"
              />
              <ValidatedField label="Line Code" id="virtual-product-line-lineCode" name="lineCode" data-cy="lineCode" type="text" />
              <ValidatedField
                id="virtual-product-line-virtualProduct"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/virtual-product-line" replace color="info">
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

export default VirtualProductLineUpdate;
