import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemuserComponent } from '../list/systemuser.component';
import { SystemuserDetailComponent } from '../detail/systemuser-detail.component';
import { SystemuserUpdateComponent } from '../update/systemuser-update.component';
import { SystemuserRoutingResolveService } from './systemuser-routing-resolve.service';

const systemuserRoute: Routes = [
  {
    path: '',
    component: SystemuserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemuserDetailComponent,
    resolve: {
      systemuser: SystemuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemuserUpdateComponent,
    resolve: {
      systemuser: SystemuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemuserUpdateComponent,
    resolve: {
      systemuser: SystemuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemuserRoute)],
  exports: [RouterModule],
})
export class SystemuserRoutingModule {}
