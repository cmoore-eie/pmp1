import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product-contract.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductContractDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductContractEntity = useAppSelector(state => state.virtualProductContract.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductContractDetailsHeading">VirtualProductContract</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductContractEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{virtualProductContractEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{virtualProductContractEntity.itemIdentifier}</dd>
          <dt>
            <span id="priority">Priority</span>
          </dt>
          <dd>{virtualProductContractEntity.priority}</dd>
          <dt>Contract</dt>
          <dd>{virtualProductContractEntity.contract ? virtualProductContractEntity.contract.id : ''}</dd>
          <dt>Virtual Product</dt>
          <dd>{virtualProductContractEntity.virtualProduct ? virtualProductContractEntity.virtualProduct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product-contract" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product-contract/${virtualProductContractEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductContractDetail;
