import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'target-system',
        data: { pageTitle: 'TargetSystems' },
        loadChildren: () => import('./target-system/target-system.module').then(m => m.TargetSystemModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'Departments' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'Employees' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'target-system-credentials',
        data: { pageTitle: 'TargetSystemCredentials' },
        loadChildren: () =>
          import('./target-system-credentials/target-system-credentials.module').then(m => m.TargetSystemCredentialsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
