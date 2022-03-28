import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product-line.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductLineDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductLineEntity = useAppSelector(state => state.virtualProductLine.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductLineDetailsHeading">VirtualProductLine</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductLineEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{virtualProductLineEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{virtualProductLineEntity.itemIdentifier}</dd>
          <dt>
            <span id="lineAvailable">Line Available</span>
          </dt>
          <dd>{virtualProductLineEntity.lineAvailable ? 'true' : 'false'}</dd>
          <dt>
            <span id="lineCode">Line Code</span>
          </dt>
          <dd>{virtualProductLineEntity.lineCode}</dd>
          <dt>Virtual Product</dt>
          <dd>{virtualProductLineEntity.virtualProduct ? virtualProductLineEntity.virtualProduct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product-line" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product-line/${virtualProductLineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductLineDetail;
