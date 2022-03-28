import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './virtual-product-contract.reducer';
import { IVirtualProductContract } from 'app/shared/model/virtual-product-contract.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductContract = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const virtualProductContractList = useAppSelector(state => state.virtualProductContract.entities);
  const loading = useAppSelector(state => state.virtualProductContract.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="virtual-product-contract-heading" data-cy="VirtualProductContractHeading">
        Virtual Product Contracts
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Virtual Product Contract
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {virtualProductContractList && virtualProductContractList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ancestor Item Identifier</th>
                <th>Item Identifier</th>
                <th>Priority</th>
                <th>Contract</th>
                <th>Virtual Product</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {virtualProductContractList.map((virtualProductContract, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${virtualProductContract.id}`} color="link" size="sm">
                      {virtualProductContract.id}
                    </Button>
                  </td>
                  <td>{virtualProductContract.ancestorItemIdentifier}</td>
                  <td>{virtualProductContract.itemIdentifier}</td>
                  <td>{virtualProductContract.priority}</td>
                  <td>
                    {virtualProductContract.contract ? (
                      <Link to={`contract/${virtualProductContract.contract.id}`}>{virtualProductContract.contract.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {virtualProductContract.virtualProduct ? (
                      <Link to={`virtual-product/${virtualProductContract.virtualProduct.id}`}>
                        {virtualProductContract.virtualProduct.name}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductContract.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductContract.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductContract.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Virtual Product Contracts found</div>
        )}
      </div>
    </div>
  );
};

export default VirtualProductContract;
