package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;


public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescriptiom = aCommand.description();
        final var isActive = aCommand.isActive();
        final var notification = Notification.create();
        final var aCategory = Category.newCategory(aName,aDescriptiom,isActive);
        aCategory.validate(notification);

        return notification.hasErrors() ? Left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(Category aCategory) {
        return Try(() -> this.categoryGateway.create(aCategory)).toEither().bimap(Notification::create,CreateCategoryOutput::from);
    }
}
