import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITargetsystem } from '../targetsystem.model';

@Component({
  selector: 'jhi-targetsystem-detail',
  templateUrl: './targetsystem-detail.component.html',
})
export class TargetsystemDetailComponent implements OnInit {
  targetsystem: ITargetsystem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetsystem }) => {
      this.targetsystem = targetsystem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
