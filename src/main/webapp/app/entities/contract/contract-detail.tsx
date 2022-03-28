import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './contract.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ContractDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const contractEntity = useAppSelector(state => state.contract.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contractDetailsHeading">Contract</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{contractEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{contractEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{contractEntity.code}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{contractEntity.itemIdentifier}</dd>
          <dt>
            <span id="itemStatus">Item Status</span>
          </dt>
          <dd>{contractEntity.itemStatus}</dd>
          <dt>
            <span id="locked">Locked</span>
          </dt>
          <dd>{contractEntity.locked ? 'true' : 'false'}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{contractEntity.name}</dd>
          <dt>
            <span id="productCode">Product Code</span>
          </dt>
          <dd>{contractEntity.productCode}</dd>
          <dt>
            <span id="versionNumber">Version Number</span>
          </dt>
          <dd>{contractEntity.versionNumber}</dd>
        </dl>
        <Button tag={Link} to="/contract" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contract/${contractEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContractDetail;
