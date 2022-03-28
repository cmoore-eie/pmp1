import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './virtual-product-organization.reducer';
import { IVirtualProductOrganization } from 'app/shared/model/virtual-product-organization.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductOrganization = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const virtualProductOrganizationList = useAppSelector(state => state.virtualProductOrganization.entities);
  const loading = useAppSelector(state => state.virtualProductOrganization.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="virtual-product-organization-heading" data-cy="VirtualProductOrganizationHeading">
        Virtual Product Organizations
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Virtual Product Organization
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {virtualProductOrganizationList && virtualProductOrganizationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ancestor Item Identifier</th>
                <th>Item Identifier</th>
                <th>Organization</th>
                <th>Producer Code</th>
                <th>Virtual Product</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {virtualProductOrganizationList.map((virtualProductOrganization, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${virtualProductOrganization.id}`} color="link" size="sm">
                      {virtualProductOrganization.id}
                    </Button>
                  </td>
                  <td>{virtualProductOrganization.ancestorItemIdentifier}</td>
                  <td>{virtualProductOrganization.itemIdentifier}</td>
                  <td>{virtualProductOrganization.organization}</td>
                  <td>{virtualProductOrganization.producerCode}</td>
                  <td>
                    {virtualProductOrganization.virtualProduct ? (
                      <Link to={`virtual-product/${virtualProductOrganization.virtualProduct.id}`}>
                        {virtualProductOrganization.virtualProduct.name}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductOrganization.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductOrganization.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductOrganization.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Virtual Product Organizations found</div>
        )}
      </div>
    </div>
  );
};

export default VirtualProductOrganization;
