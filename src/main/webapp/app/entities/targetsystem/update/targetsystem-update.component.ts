import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITargetsystem, Targetsystem } from '../targetsystem.model';
import { TargetsystemService } from '../service/targetsystem.service';

@Component({
  selector: 'jhi-targetsystem-update',
  templateUrl: './targetsystem-update.component.html',
})
export class TargetsystemUpdateComponent implements OnInit {
  isSaving = false;
  type = 'none';
  types = ['CSV', 'LDAB', 'Datenbank'];
  url = 'EMPTY';
  username = '';
  password = '';
  attributes = ['Id', 'Username', 'Password', 'Systemuser', 'Targetsystem'];
  attributesString = '';
  attributesFinal = this.attributes;
  attributesBoolean = [true, true, true, true, true];
  userDn = '';
  baseDn = '';
  tryin: any;
  editForm = this.fb.group({
    id: [],
    name: [],
    type: [],
    url: [],
    password: [],
    username: [],
    csvAttributes: [],
    user_dn: [],
    base_dn: [],
  });

  constructor(protected targetsystemService: TargetsystemService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.attributes = ['Id', 'Username', 'Password', 'Systemuser', 'Targetsystem'];
    this.activatedRoute.data.subscribe(({ targetsystem }) => {
      this.updateForm(targetsystem);
    });
  }

  previousState(): void {
    window.history.back();
  }

  onCheckChange2(): void {
    this.attributesString = '';
    for (let i = 0; i < this.attributesBoolean.length; i++) {
      if (!this.attributesBoolean[i]) {
        this.attributesFinal[i] = '';
      } else {
        this.attributesString += this.attributes[i] + ',';
        this.attributesFinal[i] = this.attributes[i];
      }
    }
  }

  onCheckChange(checked: boolean, id: number): void {
    this.attributesString = '';
    this.attributesBoolean[id] = checked;
    for (let i = 0; i < this.attributesBoolean.length; i++) {
      if (!this.attributesBoolean[i]) {
        this.attributesFinal[i] = '';
      } else {
        this.attributesString += this.attributes[i] + ',';
        this.attributesFinal[i] = this.attributes[i];
      }
    }
  }

  save(): void {
    this.onCheckChange2();
    this.isSaving = true;
    const targetsystem = this.createFromForm();
    if (targetsystem.type !== 'csv') {
      targetsystem.csvAttributes = '';
    }
    if (targetsystem.id !== undefined) {
      this.subscribeToSaveResponse(this.targetsystemService.update(targetsystem));
    } else {
      this.subscribeToSaveResponse(this.targetsystemService.create(targetsystem));
    }
  }

  selectType(id: any): void {
    if (id.value !== 'auswählen') {
      this.type = id.value;
    }
  }

  generateDbConnection(): void {
    const targetsystem = this.getTargetSystem();
    if (targetsystem.id != null && targetsystem.url != null && targetsystem.username != null && targetsystem.password != null) {
      location.href =
        'http://localhost:9000/api/targetsystemcredentials/database/' +
        targetsystem.id.toString() +
        '/' +
        targetsystem.url +
        ',' +
        targetsystem.username +
        ',' +
        targetsystem.password;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetsystem>>): void {
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

  protected updateForm(targetsystem: ITargetsystem): void {
    this.editForm.patchValue({
      id: targetsystem.id,
      name: targetsystem.name,
      type: targetsystem.type,
      url: targetsystem.url,
      password: targetsystem.password,
      username: targetsystem.username,
      csvAttribute: targetsystem.csvAttributes,
      userDn: targetsystem.userDn,
      baseDn: targetsystem.baseDn,
    });
  }

  protected getTargetSystem(): ITargetsystem {
    return {
      ...new Targetsystem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      url: this.editForm.get(['url'])!.value,
      password: this.editForm.get(['password'])!.value,
      username: this.editForm.get(['username'])!.value,
      csvAttributes: this.attributesString,
      userDn: this.editForm.get(['user_dn'])!.value,
      baseDn: this.editForm.get(['base_dn'])!.value,
    };
  }

  protected createFromForm(): ITargetsystem {
    if (this.editForm.get(['type'])!.value === 'db') {
      this.generateDbConnection();
    }
    if (this.editForm.get(['type'])!.value !== 'csv') {
      this.attributesString = '';
    }

    return {
      ...new Targetsystem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      url: this.editForm.get(['url'])!.value,
      password: this.editForm.get(['password'])!.value,
      username: this.editForm.get(['username'])!.value,
      csvAttributes: this.attributesString,
      userDn: this.editForm.get(['user_dn'])!.value,
      baseDn: this.editForm.get(['base_dn'])!.value,
    };
  }
}
