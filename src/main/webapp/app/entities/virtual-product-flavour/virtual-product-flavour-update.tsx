import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IVirtualProductCategory } from 'app/shared/model/virtual-product-category.model';
import { getEntities as getVirtualProductCategories } from 'app/entities/virtual-product-category/virtual-product-category.reducer';
import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { getEntities as getVirtualProducts } from 'app/entities/virtual-product/virtual-product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './virtual-product-flavour.reducer';
import { IVirtualProductFlavour } from 'app/shared/model/virtual-product-flavour.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { VirtualFlavourAction } from 'app/shared/model/enumerations/virtual-flavour-action.model';

export const VirtualProductFlavourUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const virtualProductCategories = useAppSelector(state => state.virtualProductCategory.entities);
  const virtualProducts = useAppSelector(state => state.virtualProduct.entities);
  const virtualProductFlavourEntity = useAppSelector(state => state.virtualProductFlavour.entity);
  const loading = useAppSelector(state => state.virtualProductFlavour.loading);
  const updating = useAppSelector(state => state.virtualProductFlavour.updating);
  const updateSuccess = useAppSelector(state => state.virtualProductFlavour.updateSuccess);
  const virtualFlavourActionValues = Object.keys(VirtualFlavourAction);
  const handleClose = () => {
    props.history.push('/virtual-product-flavour');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getVirtualProductCategories({}));
    dispatch(getVirtualProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...virtualProductFlavourEntity,
      ...values,
      category: virtualProductCategories.find(it => it.id.toString() === values.category.toString()),
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
          grandfathering: 'GRANDFATHERSTD',
          ...virtualProductFlavourEntity,
          category: virtualProductFlavourEntity?.category?.id,
          virtualProduct: virtualProductFlavourEntity?.virtualProduct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.virtualProductFlavour.home.createOrEditLabel" data-cy="VirtualProductFlavourCreateUpdateHeading">
            Create or edit a VirtualProductFlavour
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
                <ValidatedField name="id" required readOnly id="virtual-product-flavour-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Ancestor Item Identifier"
                id="virtual-product-flavour-ancestorItemIdentifier"
                name="ancestorItemIdentifier"
                data-cy="ancestorItemIdentifier"
                type="text"
              />
              <ValidatedField label="Code" id="virtual-product-flavour-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Condition" id="virtual-product-flavour-condition" name="condition" data-cy="condition" type="text" />
              <ValidatedField
                label="Default Flavour"
                id="virtual-product-flavour-defaultFlavour"
                name="defaultFlavour"
                data-cy="defaultFlavour"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Effective Date"
                id="virtual-product-flavour-effectiveDate"
                name="effectiveDate"
                data-cy="effectiveDate"
                type="date"
              />
              <ValidatedField
                label="Expiration Date"
                id="virtual-product-flavour-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="date"
              />
              <ValidatedField
                label="Grandfathering"
                id="virtual-product-flavour-grandfathering"
                name="grandfathering"
                data-cy="grandfathering"
                type="select"
              >
                {virtualFlavourActionValues.map(virtualFlavourAction => (
                  <option value={virtualFlavourAction} key={virtualFlavourAction}>
                    {virtualFlavourAction}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Item Identifier"
                id="virtual-product-flavour-itemIdentifier"
                name="itemIdentifier"
                data-cy="itemIdentifier"
                type="text"
              />
              <ValidatedField label="Line Code" id="virtual-product-flavour-lineCode" name="lineCode" data-cy="lineCode" type="text" />
              <ValidatedField label="Name" id="virtual-product-flavour-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Priority" id="virtual-product-flavour-priority" name="priority" data-cy="priority" type="text" />
              <ValidatedField label="Rank" id="virtual-product-flavour-rank" name="rank" data-cy="rank" type="text" />
              <ValidatedField id="virtual-product-flavour-category" name="category" data-cy="category" label="Category" type="select">
                <option value="" key="0" />
                {virtualProductCategories
                  ? virtualProductCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="virtual-product-flavour-virtualProduct"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/virtual-product-flavour" replace color="info">
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

export default VirtualProductFlavourUpdate;
