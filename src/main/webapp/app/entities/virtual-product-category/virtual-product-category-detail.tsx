import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product-category.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductCategoryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductCategoryEntity = useAppSelector(state => state.virtualProductCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductCategoryDetailsHeading">VirtualProductCategory</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductCategoryEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{virtualProductCategoryEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{virtualProductCategoryEntity.code}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{virtualProductCategoryEntity.itemIdentifier}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{virtualProductCategoryEntity.name}</dd>
          <dt>
            <span id="priority">Priority</span>
          </dt>
          <dd>{virtualProductCategoryEntity.priority}</dd>
          <dt>Virtual Product</dt>
          <dd>{virtualProductCategoryEntity.virtualProduct ? virtualProductCategoryEntity.virtualProduct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product-category/${virtualProductCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductCategoryDetail;
