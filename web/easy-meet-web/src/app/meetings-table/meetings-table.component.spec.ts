import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetingsTableComponent } from './meetings-table.component';

describe('MeetingsTableComponent', () => {
  let component: MeetingsTableComponent;
  let fixture: ComponentFixture<MeetingsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MeetingsTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeetingsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
