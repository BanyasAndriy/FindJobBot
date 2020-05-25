package finder.bot;

import finder.model.User;
import finder.utils.Jobs;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import finder.model.Vacancy;
import finder.service.UserService;

import java.util.*;


public enum BotState {


    Start {
        @Override
        public void enter(BotContext context, UserService userService) {
            sendMessage(context, "Привіт.Я допоможу знайти тобі підходящу вакансію.\nДля продовження введіть 'ок' ");
        }


        @Override
        public BotState nextState(BotContext context) {
            User user = context.getUser();
            if (context.getUser().getIsNewUser()) {
                context.getUser().setIsNewUser(false);
                return FindVacancy;
            } else {
                return Finish;
            }

        }
    },


   /* InputVacancy {

        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введіть позицію  : ");
        }

        @Override
        public void handleInput(BotContext context) {

            String position = context.getInput();
            context.getUser().setPosition(position);

        }

        @Override
        public BotState nextState(BotContext context) {
            return City;
        }
    },


    City {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введіть місто у якому ви б хотіли працювати :");
        }

        @Override
        public void handleInput(BotContext context) {

            String city = context.getInput();
            context.getUser().setCity(city);

        }

        @Override
        public BotState nextState(BotContext botContext) {
            return FindVacancy;
        }
    },*/

    FindVacancy {
        private BotState next;

        @Override
        public void handleInput(BotContext context, UserService userService) {
            sendMessage(context, "Почекайте хвилинку! Запрос опрацьовується !");
            User user = context.getUser();

            List<Vacancy> vacancyList = new ArrayList<>();
            vacancyList.addAll(Jobs.getJobsFromDou("", ""));
            vacancyList.addAll(Jobs.getJobsFromWorkUa("", ""));
            vacancyList.forEach(vacancy -> vacancy.addUser(user));
            user.setVacancyList(vacancyList);
            userService.updateUser(user);
            if (vacancyList.isEmpty()) {
                sendMessage(context, "На даний момент вільних вакансій немає!\nЯк тільки появляться нові вакансії ми вас повідомимо!");
            } else {
                for (Vacancy vacancy : vacancyList
                ) {
                    sendMessage(context, "Позиція : " + vacancy.getName() + "\nСилка на ваканцію : " + vacancy.getUrl());
                    System.out.println("Позиція : " + vacancy.getName() + "\nСилка на ваканцію : " + vacancy.getUrl());
                }
            }
        }


        @Override
        public BotState nextState(BotContext botContext) {
            return Approved;
        }
    },
 /*   GetNewVacancy {
        private BotState next;

        @Override
        public void handleInput(BotContext context) {
            sendMessage(context, "Почекайте хвилинку! Шукаємо вакансії,які ви не бачили!");


            List<Vacancy> vacancyList = Jobs.getJobsFromDou("", "");

            if (vacancyList.isEmpty()) {
                sendMessage(context, "На даний момент вільних вакансій немає!");
            } else {
                for (Vacancy vacancy : vacancyList
                ) {
                    sendMessage(context, "Позиція : " + vacancy.getName() + "\nСилка на ваканцію : " + vacancy.getUrl());
                    System.out.println("Позиція : " + vacancy.getName() + "\nСилка на ваканцію : " + vacancy.getUrl());
                }
            }

        }


        @Override
        public BotState nextState(BotContext botContext) {
            return Approved;
        }
    },*/

    Approved(false) {
        @Override
        public void enter(BotContext context, UserService userService) {
            sendMessage(context, "Дякуємо,що скористувались нашим ботом!" +
                    "Як тільки з'являться нові вакансії ми ваc повідомимо!");
        }

        @Override
        public BotState nextState(BotContext botContext) {
            return Finish;
        }
    },
    Finish {
        @Override
        public void enter(BotContext context, UserService userService) {
            User user = context.getUser();

            while (true) {

                try {
                    Thread.sleep(60000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Vacancy> vacancyList = new ArrayList<>();
                vacancyList.addAll(Jobs.getOnlyNewVacancy(Jobs.getJobsFromDou("", ""), user));
                vacancyList.addAll(Jobs.getOnlyNewVacancy(Jobs.getJobsFromWorkUa("", ""), user));
                if (!vacancyList.isEmpty()) {
                    user.setVacancyList(vacancyList);
                    userService.updateUser(user);
                }

                if (!vacancyList.isEmpty()) {
                    sendMessage(context, "Нова вакансія!");
                    for (Vacancy vacancy : vacancyList
                    ) {
                        sendMessage(context, "Позиція : " + vacancy.getName() + "\nСилка на ваканцію : " + vacancy.getUrl());
                        System.out.println("Позиція : " + vacancy.getName() + "\nСилка на ваканцію : " + vacancy.getUrl());
                    }
                }else {
                    sendMessage(context, "Протягом години нові вакансії не появились!");
                }
            }
        }
    };


    private static BotState[] states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }

        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context, UserService userService) {
        // do nothing by default
    }


    public void enter(BotContext context, UserService userService) {
        // do nothing by default

    }

    /*    public  BotState nextState(){
            return null;
        };*/
//need to override
    public BotState nextState(BotContext context) {
        return null;
    }

    ;
}
