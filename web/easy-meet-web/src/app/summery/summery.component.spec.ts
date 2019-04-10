import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SummeryComponent } from './summery.component';

describe('SummeryComponent', () => {
  let component: SummeryComponent;
  let fixture: ComponentFixture<SummeryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SummeryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SummeryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
