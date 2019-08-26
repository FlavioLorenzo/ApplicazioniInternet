import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildrenRegistrationComponent } from './children-registration.component';

describe('ChildrenRegistrationComponent', () => {
  let component: ChildrenRegistrationComponent;
  let fixture: ComponentFixture<ChildrenRegistrationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChildrenRegistrationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChildrenRegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
