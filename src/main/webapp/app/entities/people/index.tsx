import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import People from './people';
import PeopleDetail from './people-detail';
import PeopleUpdate from './people-update';
import PeopleDeleteDialog from './people-delete-dialog';

const PeopleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<People />} />
    <Route path="new" element={<PeopleUpdate />} />
    <Route path=":id">
      <Route index element={<PeopleDetail />} />
      <Route path="edit" element={<PeopleUpdate />} />
      <Route path="delete" element={<PeopleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PeopleRoutes;
