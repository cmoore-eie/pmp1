import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { getEntities as getVirtualProducts } from 'app/entities/virtual-product/virtual-product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './virtual-product-category.reducer';
import { IVirtualProductCategory } from 'app/shared/model/virtual-product-category.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductCategoryUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const virtualProducts = useAppSelector(state => state.virtualProduct.entities);
  const virtualProductCategoryEntity = useAppSelector(state => state.virtualProductCategory.entity);
  const loading = useAppSelector(state => state.virtualProductCategory.loading);
  const updating = useAppSelector(state => state.virtualProductCategory.updating);
  const updateSuccess = useAppSelector(state => state.virtualProductCategory.updateSuccess);
  const handleClose = () => {
    props.history.push('/virtual-product-category');
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
      ...virtualProductCategoryEntity,
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
          ...virtualProductCategoryEntity,
          virtualProduct: virtualProductCategoryEntity?.virtualProduct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.virtualProductCategory.home.createOrEditLabel" data-cy="VirtualProductCategoryCreateUpdateHeading">
            Create or edit a VirtualProductCategory
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
                <ValidatedField name="id" required readOnly id="virtual-product-category-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Ancestor Item Identifier"
                id="virtual-product-category-ancestorItemIdentifier"
                name="ancestorItemIdentifier"
                data-cy="ancestorItemIdentifier"
                type="text"
              />
              <ValidatedField label="Code" id="virtual-product-category-code" name="code" data-cy="code" type="text" />
              <ValidatedField
                label="Item Identifier"
                id="virtual-product-category-itemIdentifier"
                name="itemIdentifier"
                data-cy="itemIdentifier"
                type="text"
              />
              <ValidatedField label="Name" id="virtual-product-category-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Priority" id="virtual-product-category-priority" name="priority" data-cy="priority" type="text" />
              <ValidatedField
                id="virtual-product-category-virtualProduct"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/virtual-product-category" replace color="info">
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

export default VirtualProductCategoryUpdate;
