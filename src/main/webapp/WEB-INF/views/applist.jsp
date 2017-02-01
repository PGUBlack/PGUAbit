<!-- обратите внимание на spring тэги -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<html>
<head>
  <title>Заполнение карточки абитуриента | АСУ «Абитуриент» 2.0</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Material Design Lite -->
  <script src="https://code.getmdl.io/1.2.1/material.min.js"></script>
  <link rel="stylesheet" href="https://code.getmdl.io/1.2.1/material.indigo-pink.min.css">
    <!-- Material Design icon font -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://cdn.rawgit.com/CreativeIT/getmdl-select/master/getmdlмл
    -select.min.css">
    <script defer src="https://cdn.rawgit.com/CreativeIT/getmdl-select/master/getmdl-select.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>
<!-- Always shows a header, even in smaller screens. -->
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
  <header class="mdl-layout__header">
    <div class="mdl-layout__header-row">
      <!-- Title -->
      <span class="mdl-layout-title">Заполнение карточки абитуриента</span>
      <!-- Add spacer, to align navigation to the right -->
      <div class="mdl-layout-spacer"></div>
      <!-- Navigation. We hide it in small screens. -->
      <nav class="mdl-navigation mdl-layout--large-screen-only">
        <a class="mdl-navigation__link" href="">Отчеты</a>
        <a class="mdl-navigation__link" href="">Поиск</a>
        <a class="mdl-navigation__link" href="">ФИС</a>
        <a class="mdl-navigation__link" href="">Экзамены</a>
      </nav>
    </div>
  </header>
  <div class="mdl-layout__drawer">
    <span class="mdl-layout-title">Меню</span>
    <nav class="mdl-navigation">
      <a class="mdl-navigation__link" href="">Отчеты</a>
      <a class="mdl-navigation__link" href="">Поиск</a>
      <a class="mdl-navigation__link" href="">ФИС</a>
      <a class="mdl-navigation__link" href="">Экзамены</a>
    </nav>
  </div>
  <main class="mdl-layout__content">
      <div class="page-content">
        <spring:form method="post" modelAttribute="userJSP" action="check-user" accept-charset="UTF-8">
            <div class="card-wide mdl-shadow--3dp">
                <h4>1. Личные данные / Документы</h4>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <spring:input path="lastname" cssClass="mdl-textfield__input" id="abit_lastname"/>
                    <label class="mdl-textfield__label" for="abit_lastname">Фамилия</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <spring:input path="name" cssClass="mdl-textfield__input" id="abit_name"/>
                    <label class="mdl-textfield__label" for="abit_name">Имя</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <spring:input path="secondname" cssClass="mdl-textfield__input" id="abit_secondname"/>
                    <label class="mdl-textfield__label" for="abit_secondname">Отчество</label>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="country" name="country" value="Россия" type="text" readonly tabIndex="-1" data-val="RUS"/>
                        <label class="mdl-textfield__label" for="country">Гражданство</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="country">
                            <li class="mdl-menu__item" data-val="RUS">Российская Федерация</li>
                            <li class="mdl-menu__item" data-val="KAZ">Казахстан</li>
                            <li class="mdl-menu__item" data-val="BLR">Республика Беларусь</li>
                            <li class="mdl-menu__item" data-val="CHI">Китай</li>
                        </ul>
                    </div>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="abit_birthdate">
                    <label class="mdl-textfield__label" for="abit_birthdate">Дата рождения</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="abit_birthplace">
                    <label class="mdl-textfield__label" for="abit_birthplace">Место рождения</label>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="gender" name="gender" value="-" type="text" readonly tabIndex="-1" data-val="-"/>
                        <label class="mdl-textfield__label" for="gender">Пол</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="gender">
                            <li class="mdl-menu__item" data-val="-">-</li>
                            <li class="mdl-menu__item" data-val="male">муж</li>
                            <li class="mdl-menu__item" data-val="female">жен</li>
                        </ul>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="passport" name="passport" value="Паспорт" type="text" readonly tabIndex="-1" data-val="passport"/>
                        <label class="mdl-textfield__label" for="passport">Тип документа</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="passport">
                            <li class="mdl-menu__item" data-val="passport">Паспорт</li>
                            <li class="mdl-menu__item" data-val="int_passport">Загран. паспорт</li>
                        </ul>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_doc_series">
                        <label class="mdl-textfield__label" for="abit_doc_series">Серия документа</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_doc_number">
                        <label class="mdl-textfield__label" for="abit_doc_number">Номер документа</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_doc_who">
                        <label class="mdl-textfield__label" for="abit_doc_who">Кем выдан</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_doc_code">
                        <label class="mdl-textfield__label" for="abit_doc_code">Код подразделения</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_doc_date">
                        <label class="mdl-textfield__label" for="abit_doc_date">Дата выдачи</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="is_crimea" name="is_crimea" value="нет" type="text" readonly tabIndex="-1" data-val="no"/>
                        <label class="mdl-textfield__label" for="is_crimea">Житель Республики Крым</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="is_crimea">
                            <li class="mdl-menu__item" data-val="no">нет</li>
                            <li class="mdl-menu__item" data-val="yes">да</li>
                        </ul>
                    </div>
                </div>
                <button class="mdl-button mdl-js-button mdl-button--fab" disabled>
                    <i class="material-icons">add</i>
                </button>
            </div>
            <div class="card-wide mdl-shadow--3dp">
                <h4>2. Специальности / Направления подготовки</h4>
            </div>
            <div class="card-wide mdl-shadow--3dp">
                <h4>3. Информация о баллах по конкурсным предметам</h4>
            </div>
            <div class="card-wide mdl-shadow--3dp">
                <h4>4. Индивидуальные достижения</h4>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="ach" name="ach" value="Участие в соревнованиях" type="text" readonly tabIndex="-1" data-val="sports"/>
                        <label class="mdl-textfield__label" for="ach">Наименование достижения</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="ach">
                            <li class="mdl-menu__item" data-val="sports">Участие в соревнованиях</li>
                            <li class="mdl-menu__item" data-val="olymp">Участие в олимпиадах</li>
                        </ul>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_ach_results">
                        <label class="mdl-textfield__label" for="abit_ach_results">Кол-во баллов</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="ach2" name="ach2" value="Участие в олимпиадах" type="text" readonly tabIndex="-1" data-val="olymp"/>
                        <label class="mdl-textfield__label" for="ach2">Наименование достижения</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="ach2">
                            <li class="mdl-menu__item" data-val="sports">Участие в соревнованиях</li>
                            <li class="mdl-menu__item" data-val="olymp">Участие в олимпиадах</li>
                        </ul>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_ach_results2">
                        <label class="mdl-textfield__label" for="abit_ach_results2">Кол-во баллов</label>
                    </div>
                </div>
                <button class="mdl-button mdl-js-button mdl-button--fab" disabled>
                    <i class="material-icons">add</i>
                </button>
            </div>
            <div class="card-wide mdl-shadow--3dp">
                <h4>5. Дополнительная информация</h4>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="abit_info_return" name="abit_info_return" value="лично" type="text" readonly tabIndex="-1" data-val="person"/>
                        <label class="mdl-textfield__label" for="abit_info_return">Способ возврата документов</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="abit_info_return">
                            <li class="mdl-menu__item" data-val="person">лично</li>
                            <li class="mdl-menu__item" data-val="post">через оператор почтовой связи</li>
                        </ul>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="abit_info_needHostel" name="abit_info_needHostel" value="-" type="text" readonly tabIndex="-1" data-val="-"/>
                        <label class="mdl-textfield__label" for="abit_info_needHostel">Потребность в общежитии</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="abit_info_needHostel">
                            <li class="mdl-menu__item" data-val="-">-</li>
                            <li class="mdl-menu__item" data-val="no">нет</li>
                            <li class="mdl-menu__item" data-val="yes">да</li>
                        </ul>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select">
                        <input class="mdl-textfield__input" id="abit_info_lang" name="abit_info_lang" value="английский" type="text" readonly tabIndex="-1" data-val="en"/>
                        <label class="mdl-textfield__label" for="abit_info_lang">Ин. яз.</label>
                        <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="abit_info_lang">
                            <li class="mdl-menu__item" data-val="en">английский</li>
                            <li class="mdl-menu__item" data-val="de">немецкий</li>
                            <li class="mdl-menu__item" data-val="fr">французский</li>
                        </ul>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_phoneMob">
                        <label class="mdl-textfield__label" for="abit_info_phoneMob">Контактный телефон</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_examLang">
                        <label class="mdl-textfield__label" for="abit_info_examLang">Вступ. исп. на ин. языке</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_email">
                        <label class="mdl-textfield__label" for="abit_info_email">E-mail</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_distPlace">
                        <label class="mdl-textfield__label" for="abit_info_distPlace">Место сдачи вступ. исп. дист.</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_post">
                        <label class="mdl-textfield__label" for="abit_info_post" >Почтовый адрес</label>
                    </div>
                </div>
                <div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_distDate">
                        <label class="mdl-textfield__label" for="abit_info_distDate">Дата сдачи вступ. исп. дист.</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="abit_info_specialCondition">
                        <label class="mdl-textfield__label" for="abit_info_specialCondition">Обеспечение специальных условий</label>
                    </div>
                </div>
            </div>
            <div class="submit">
                <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" disabled>Cохранить</button>
            </div>
        </spring:form>
      </div>
  </main>
</div>
<style type="text/css">
    .mdl-layout__header .material-icons {
        line-height: 2!important;
    }
    .page-content {
        margin: 20px;
    }
    .mdl-layout__content {
        background-color: #f1f1f1;
    }
    .getmdl-select input {
        cursor: pointer;
    }
    .getmdl-select .mdl-menu__container {
        box-shadow: 0 3px 4px 0 rgba(0,0,0,.14), 0 3px 3px -2px rgba(0,0,0,.2), 0 1px 8px 0 rgba(0,0,0,.12);
    }
    .getmdl-select ul {
         background-color: #f5f5f5;
     }
    .submit {
        margin-top: 20px;
    }
    .card-wide {
        padding: 5px 30px 15px 30px;
        background-color: #ffffff;
        margin: 20px 0;
    }
    .mdl-textfield__label {
        color: rgba(0,0,0,.48);
    }
</style>
<script type="text/javascript">
    $( function() {
        var availableEst = [
            'Пензенская государственная сельскохозяйственная академия',
            'Пензенская государственная технологическая академия',
            'Пензенский артиллерийский инженерный институт имени Главного маршала артиллерии Н.Н. Воронова (филиал) Военного учебно-научного центра Сухопутных войск "Общевойсковая академия Вооруженных Сил Российской Федерации"',
            'Пензенский Государственный педагогический университет имени В.Г. Белинского',
            'Пензенский государственный университет',
            'Пензенский государственный университет архитектуры и строительства',
            'Пензенский институт технологий и бизнеса (филиал) Московского государственного университетатехнологий и управления',
            'Пензенский региональный центр высшей школы (филиал) Российского Государственного Университета Инновационных Технологий и Предпринимательства',
            'Пензенский филиал академии МНЭПУ',
            'Пензенский филиал Московской открытой социальной академии',
            'Пензенский филиал Современной гуманитарной академии',
            'Колледж парикмахерского искусства и эстетики "Бьюти-Профи"',
            'Пензенский автомобильно-дорожный колледж',
            'Пензенский аграрный техникум',
            'Пензенский архитектурно-строительный колледж',
            'Пензенский базовый медицинский колледж Министерства здравоохранения и социального развития Российской Федерации',
            'Пензенский государственный приборостроительный коледж',
            'Пензенский колледж культуры и искусств',
            'Пензенский колледж управления и промышленных технологий им. Е.Д. Басулина',
            'Пензенский машиностроительный колледж',
            'Пензенский многопрофильный колледж торгово-экономическое отделение (ГБОУ СПО ПО ПМПК ТЭО)',
            'Пензенский областной медицинский колледж',
            'Пензенский профессионально-педагогический колледж',
            'Пензенский техникум железнодорожного транспорта',
            'Пензенский техникум сферы быта и услуг',
            'Пензенский филиал заочного обучения Саратовского юридического института',
            'Пензенское музыкальное училище имени А.А. Архангельского',
            'Пензенское художественное училище им. К.А. Савицкого'
        ];
        $( "#abit_doc_who" ).autocomplete({
            source: availableEst
        });
    } );
</script>
</body>
</html>