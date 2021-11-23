import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISystemuser, Systemuser } from '../systemuser.model';
import { SystemuserService } from '../service/systemuser.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'jhi-systemuser-update',
  templateUrl: './systemuser-update.component.html',
})
export class SystemuserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    entryDate: [],
    name: [],
    socialSecurityNumber: [],
    jobDescription: [],
    user: [null, Validators.required],
    departments: [],
  });

  constructor(
    protected systemuserService: SystemuserService,
    protected userService: UserService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemuser }) => {
      this.updateForm(systemuser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemuser = this.createFromForm();
    if (systemuser.id !== undefined) {
      this.subscribeToSaveResponse(this.systemuserService.update(systemuser));
    } else {
      this.subscribeToSaveResponse(this.systemuserService.create(systemuser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  getSelectedDepartment(option: IDepartment, selectedVals?: IDepartment[]): IDepartment {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemuser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(systemuser: ISystemuser): void {
    this.editForm.patchValue({
      id: systemuser.id,
      entryDate: systemuser.entryDate,
      name: systemuser.name,
      socialSecurityNumber: systemuser.socialSecurityNumber,
      jobDescription: systemuser.jobDescription,
      user: systemuser.user,
      departments: systemuser.departments,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, systemuser.user);
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      ...(systemuser.departments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, ...(this.editForm.get('departments')!.value ?? []))
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): ISystemuser {
    return {
      ...new Systemuser(),
      id: this.editForm.get(['id'])!.value,
      entryDate: this.editForm.get(['entryDate'])!.value,
      name: this.editForm.get(['name'])!.value,
      socialSecurityNumber: this.editForm.get(['socialSecurityNumber'])!.value,
      jobDescription: this.editForm.get(['jobDescription'])!.value,
      user: this.editForm.get(['user'])!.value,
      departments: this.editForm.get(['departments'])!.value,
    };
  }
}
