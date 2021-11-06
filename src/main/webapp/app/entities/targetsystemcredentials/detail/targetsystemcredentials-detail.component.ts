import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITargetsystemcredentials } from '../targetsystemcredentials.model';

@Component({
  selector: 'jhi-targetsystemcredentials-detail',
  templateUrl: './targetsystemcredentials-detail.component.html',
})
export class TargetsystemcredentialsDetailComponent implements OnInit {
  targetsystemcredentials: ITargetsystemcredentials | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetsystemcredentials }) => {
      this.targetsystemcredentials = targetsystemcredentials;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
